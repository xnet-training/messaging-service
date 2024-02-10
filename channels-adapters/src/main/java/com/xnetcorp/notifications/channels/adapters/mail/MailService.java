package com.xnetcorp.notifications.channels.adapters.mail;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${notification.mail.from}")
    private String mailFrom;

    @Override
    @Timed
    public void send(NotificationMessage message) {
        log.info(message.toString());
        log.info("[Mail] Enviando mensaje Mail '" + message.getMessage() + "' al correo " + message.getProperties().get("mail"));
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom(mailFrom);
            helper.setTo((String)message.getProperties().get("mail"));
            helper.setSubject((String)Optional.ofNullable(message.getProperties().get("subject")).orElse("Sin titulo"));
            helper.setText(message.getMessage(), true);

            mailSender.send(msg);

            log.info("[Mail] Mensaje enviado");
        } catch(MessagingException ex) {
            log.error(ex.getMessage());
        }
    }
}
