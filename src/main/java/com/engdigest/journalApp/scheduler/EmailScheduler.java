package com.engdigest.journalApp.scheduler;

import com.engdigest.journalApp.cache.AppCache;
import com.engdigest.journalApp.entity.JournalEntry;
import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.enums.Sentiment;
import com.engdigest.journalApp.journalrepository.UserRepositoryImpl;
import com.engdigest.journalApp.model.SentimentData;
import com.engdigest.journalApp.service.EmailService;
import com.engdigest.journalApp.service.JournalEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EmailScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    JournalEntryService entryService;

    @Autowired
    AppCache appCache;

    @Autowired
    private SentimentData sentimentData;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Autowired
    EmailService emailService;
    @Scheduled(cron="0 0/10 * ? * *")
    public void fetchUserAndSendEmail(){
        List<User> users=userRepository.getUserForSA();
        log.info("Scheduler ran, found {} eligible users", users.size());
        for(User user: users){
            List<JournalEntry> entries=user.getJournalEntries();
            List<Sentiment> sentiments=entries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();
            for(Sentiment sentiment:sentiments){
                if(sentiment!=null)
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
            }
            if(sentimentCounts.size()>0){
                Sentiment mostFrequentSentiment=null;
                int maxct=0;
                for(Map.Entry<Sentiment,Integer> entry: sentimentCounts.entrySet()){
                    if(entry.getValue()>maxct){
                        maxct= entry.getValue();
                        mostFrequentSentiment=entry.getKey();
                    }
                }
                SentimentData sentimentData=SentimentData.builder().email(user.getEmail()).sentiment(mostFrequentSentiment.toString()).build();
                try {
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }
                catch (Exception e){
                    emailService.sendEmail(user.getEmail(), "Sentiment Analysis Report Of The Week",sentimentData.getSentiment());
                }
            }
        }
    }

    @Scheduled(cron="0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
