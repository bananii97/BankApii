package com.bank.email;

import com.bank.exception.InvalidEmailFormatException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String message){

        try{
            new InternetAddress(to).validate();
        } catch (AddressException e) {
            log.warn("Mail sender failed: email incorrect {} ", to);
            throw new InvalidEmailFormatException(to, HttpStatus.BAD_REQUEST);
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try{
            mailSender.send(mailMessage);
            log.info("Mail sender sent to {} successfully", to);
        }catch(Exception e){
            log.error("Mail sender sent to {} failed", to, e);
            throw new RuntimeException("The email could not be sent to technical reasons");
        }

    }
}
