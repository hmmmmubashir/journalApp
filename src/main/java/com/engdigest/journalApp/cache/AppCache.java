package com.engdigest.journalApp.cache;

import com.engdigest.journalApp.entity.ConfigEntity;
import com.engdigest.journalApp.journalrepository.JournalConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AppCache {
    @Autowired
    JournalConfigRepository configRepository;

    public HashMap<String ,String> cache;

    @PostConstruct
    public void init(){
        cache=new HashMap<>();
        List<ConfigEntity> l=configRepository.findAll();
        for(ConfigEntity x: l){
            cache.put(x.getKey(),x.getValue());
        }
    }
}
