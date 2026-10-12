package com.engdigest.journalApp.service;

import com.engdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {
    @Autowired
    EmailService emailService;

    @KafkaListener(topics="weekly-sentiments",groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    public void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(),"Sentiment Analysis Report of the Week", sentimentData.getSentiment());
    }
}
