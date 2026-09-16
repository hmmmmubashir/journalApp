package com.engdigest.journalApp.serviceTests;

import com.engdigest.journalApp.entity.User;
import com.engdigest.journalApp.journalrepository.UserRepository;
import com.engdigest.journalApp.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Disabled
    @Test
    public void testFindByUserName(){
        assertNotNull(userService.findByUserName("ahmed"));
    }


}
