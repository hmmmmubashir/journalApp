package com.engdigest.journalApp.repoTests;

import com.engdigest.journalApp.journalrepository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepoTests {

    @Autowired
    private UserRepositoryImpl userRepository;
    @Test
    public void testforSA(){
        assertNotNull(userRepository.getUserForSA());
    }

}
