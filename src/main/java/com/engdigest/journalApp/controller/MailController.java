package com.engdigest.journalApp.controller;

import com.engdigest.journalApp.entity.MailEntity;
import com.engdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    EmailService emailService;

    @Autowired
    MailEntity mailEntity;

    @PostMapping("/send-mail")
    public void sendMail(@RequestBody MailEntity mail){
        emailService.sendEmail(mail.getTo(),mail.getSubject(),mail.getBody());
    }
}
