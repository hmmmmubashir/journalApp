package com.engdigest.journalApp.service;

import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.journalrepository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    @Autowired
    private UserRepository repouser;

    public List<User> getAll(){
        return repouser.findAll();
    }
    private PasswordEncoder encoder=new BCryptPasswordEncoder();

    public User createUser(User user){
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            return repouser.save(user);
        }
        catch (Exception e){
            log.error("Error occured for username{}:  ",user.getUsername(),e);
            //here e causes the entire  exception stack trace to be printed
            //to just want the line shown above simply remove e
        }
        return null;
    }
    public User createAdminUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        return repouser.save(user);
    }

    public User saveUser(User user){
        return repouser.save(user);
    }

    public Optional<User> getById(ObjectId id){
        return repouser.findById(id);
    }

    public void deleteEntryById(ObjectId id){
        repouser.deleteById(id);
    }

    public void deleteByUserName(String username){
        repouser.deleteByusername(username);
    }

    public User findByUserName(String username){
        return repouser.findByusername(username);
    }
}
