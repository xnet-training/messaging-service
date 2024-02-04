package com.xnetcorp.notifications.channels.adapters.mail;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.xnetcorp.notifications.channels.adapters.NotificationMessage;
import com.xnetcorp.notifications.channels.adapters.NotificationService;

import io.micrometer.core.annotation.Timed;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService implements NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Timed
    public void send(NotificationMessage message) {
        log.info(message.toString());
        log.info("Enviando mensaje Mail '" + message.getMessage() + "' al correo " + message.getProperties().get("mail"));
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom("ilver.anache@gmail.com");
            helper.setTo((String)message.getProperties().get("mail"));
            helper.setSubject((String)Optional.ofNullable(message.getProperties().get("subject")).orElse("Sin titulo"));
            helper.setText(message.getMessage());

            mailSender.send(msg);
        } catch(MessagingException ex) {
            log.error(ex.getMessage());
        }
    }
}
