package com.engdigest.journalApp.controller;

import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> l=userService.getAll();
        if(l!=null && !l.isEmpty())
            return new ResponseEntity<>(l, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-new-admin")
    public ResponseEntity<?> createNewAdmin(@RequestBody User user){
        return new ResponseEntity<>(userService.createAdminUser(user),HttpStatus.CREATED);
    }

}
