package com.shantanu.blog.service;

import com.shantanu.blog.exceptions.SpringRedditException;
import com.shantanu.blog.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final MailContentBuilder contentBuilder;

    private final JavaMailSender mailSender;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("cushantanu@gmail.com");
            helper.setTo(notificationEmail.getRecipient());
            helper.setSubject(notificationEmail.getSubject());
            helper.setText(contentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(mimeMessagePreparator);
            log.info("Activation Email Sent !");
        } catch (MailException m) {
            throw new SpringRedditException("Exception Occurred when sent mail to " + notificationEmail.getRecipient());
        }


    }

}
