package com.engdigest.journalApp.service;

import com.engdigest.journalApp.entity.JournalEntry;
import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.journalrepository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository repo;

    @Autowired
    private UserService userService;

    public JournalEntry saveEntry(JournalEntry myentry){
        return repo.save(myentry);
    }

    public List<JournalEntry> getAll(){
        return repo.findAll();
    }

    public Optional<JournalEntry> getEntrybyid(ObjectId id){
        return repo.findById(id);
    }

    public void deleteEntryById(ObjectId id, String username){
        User user=userService.findByUserName(username);
        user.getJournalEntries().removeIf(x->x.getId().equals(id));
        userService.createUser(user);
        repo.deleteById(id);
    }

}
