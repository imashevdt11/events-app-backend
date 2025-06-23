package kg.something.events_app_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.something.events_app_backend.dto.request.PaymentRequest;
import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.User;

public interface EmailService {

    MimeMessage createMailWithCheck(User user, Event event, long[] ticketNumbers, PaymentRequest paymentRequest) throws MessagingException;

    MimeMessage createMailWithConfirmationCode(User user, ConfirmationCode confirmationCode) throws MessagingException;

    void sendEmail(MimeMessage email);

    void sendEmailWithCheck(User user, Event event, long[] ticketNumbers, PaymentRequest paymentRequest);

    void sendEmailWithConfirmationCode(ConfirmationCode confirmationCode, User user);
}