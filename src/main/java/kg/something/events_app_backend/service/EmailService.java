package kg.something.events_app_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.User;

public interface EmailService {

    MimeMessage createMailWithConfirmationCode(User user, ConfirmationCode confirmationCode) throws MessagingException;

    void sendEmail(MimeMessage email);

    void sendEmailWithCheck(User user, Event event, long[] ticketNumbers);

    void sendEmailWithConfirmationCode(ConfirmationCode confirmationCode, User user);
}