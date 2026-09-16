package com.engdigest.journalApp.controller;

import com.engdigest.journalApp.entity.JournalEntry;
import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.journalrepository.JournalEntryRepository;
import com.engdigest.journalApp.service.JournalEntryService;
import com.engdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ser.std.DelegatingSerializer;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService service;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List> getAllJournalEntriesOfUser(){
        Authentication authenticatedUser= SecurityContextHolder.getContext().getAuthentication();
        String username=authenticatedUser.getName();
        User user=userService.findByUserName(username);
        List<JournalEntry> l=new ArrayList<>();
        l=user.getJournalEntries();
        if(!l.isEmpty() && l!=null){
            return new ResponseEntity<>(l,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping()
    @Transactional
    public ResponseEntity<JournalEntry> makeentry(@RequestBody JournalEntry myentry){
        try {
            //here the journal entry is getting mapped to the correct user in makeentry controller only
            //it is a better practice to write this mapping logic in journal service.....
            Authentication authenticatedUser= SecurityContextHolder.getContext().getAuthentication();
            String username=authenticatedUser.getName();
            myentry.setDate(LocalDateTime.now());
            service.saveEntry(myentry);
            User user=userService.findByUserName(username);
            user.getJournalEntries().add(myentry);
            userService.saveUser(user);
            return new ResponseEntity<>(myentry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> getjournalentrybyId(@PathVariable ObjectId myid){
        Authentication authenticatedUser= SecurityContextHolder.getContext().getAuthentication();
        String username=authenticatedUser.getName();
        User user=userService.findByUserName(username);
        for(JournalEntry l: user.getJournalEntries()){
            if(l.getId().equals(myid)) {
                Optional<JournalEntry> thisEntry = service.getEntrybyid(myid);
                return new ResponseEntity<>(thisEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> deletejournalentrybyid(@PathVariable ObjectId myid){
        Authentication authenticatedUser= SecurityContextHolder.getContext().getAuthentication();
        String username=authenticatedUser.getName();
        User user=userService.findByUserName(username);
        for(JournalEntry l: user.getJournalEntries()){
            if(l.getId().equals(myid)) {
                Optional<JournalEntry> thisEntry = service.getEntrybyid(myid);
                service.deleteEntryById(myid,username);
                userService.saveUser(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myid}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId myid,@RequestBody JournalEntry newentry){
        Authentication authenticatedUser= SecurityContextHolder.getContext().getAuthentication();
        String username=authenticatedUser.getName();
        User user=userService.findByUserName(username);

        for(JournalEntry l:user.getJournalEntries()){
            if(l.getId().equals(myid)){
                JournalEntry old=service.getEntrybyid(myid).orElse(null);
                if(old!=null){
                    old.setTitle(newentry.getTitle()!=null && !newentry.getTitle().equals("")?newentry.getTitle(): old.getTitle());
                    old.setContent(newentry.getContent()!=null && !newentry.getContent().equals("")? newentry.getContent() : old.getContent());
                    service.saveEntry(old);
                    return new ResponseEntity<>(old,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
