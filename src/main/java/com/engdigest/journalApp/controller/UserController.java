package com.engdigest.journalApp.controller;

import com.engdigest.journalApp.api.response.WeatherResponse;
import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.service.UserService;
import com.engdigest.journalApp.service.WeatherService;
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

    @Autowired
    private WeatherService weatherService;


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

    @GetMapping()
    public ResponseEntity<?> getWeather(){
        Authentication AutheticatedUser=SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse response= weatherService.getWeather("Hyderabad");
        String greeting="";
        if(response!=null)
            greeting=" weather feels like "+response.getCurrent().getFeelslike()+" with humidity "+response.getCurrent().getHumidity()+" and uv index "+response.getCurrent().getUvIndex();
            return new ResponseEntity<>("Hi "+AutheticatedUser.getName()+greeting,HttpStatus.OK);
    }



}
