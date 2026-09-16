package com.engdigest.journalApp.controller;

import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @DeleteMapping()
    public void deleteUserByusername(){
        Authentication AutheticatedUser=SecurityContextHolder.getContext().getAuthentication();
        String username=AutheticatedUser.getName();
        userService.deleteByUserName(username);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication AuthenticatedUser=SecurityContextHolder.getContext().getAuthentication();
        String username=AuthenticatedUser.getName();
        User userInDb=userService.findByUserName(username);
        //actually no need to check userInDb!=null since this is already done by authentication manager builder in SpringSecurity class
        if(userInDb!=null){
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.createUser(userInDb);
            return new ResponseEntity<>(userInDb,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
