package kg.something.events_app_backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.something.events_app_backend.dto.request.PaymentRequest;
import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.service.ConfirmationCodeService;
import kg.something.events_app_backend.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {

    private final ConfirmationCodeService confirmationCodeService;
    private final SpringTemplateEngine engine;
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(ConfirmationCodeService confirmationCodeService, SpringTemplateEngine engine, JavaMailSender javaMailSender) {
        this.confirmationCodeService = confirmationCodeService;
        this.engine = engine;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public MimeMessage createMailWithConfirmationCode(User user, ConfirmationCode confirmationCode) throws MessagingException {

        Context context = new Context();
        context.setVariable("confirmCode", confirmationCode.getCode());
        String emailBody = engine.process("confirmation_code", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(emailBody, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Код подтверждения");

        return mimeMessage;
    }

    public MimeMessage createMailWithCheck(User user, Event event, long[] ticketNumbers, PaymentRequest paymentRequest) throws MessagingException {

        Context context = new Context();
        context.setVariable("ticketNumbers", ticketNumbers);
        context.setVariable("user", user);
        context.setVariable("event", event);
        context.setVariable("checkInfo", paymentRequest);
        String emailBody = engine.process("tickets_email", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(emailBody, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Чек о покупке билетов на мероприятие");

        return mimeMessage;
    }

    @Override
    public void sendEmail(MimeMessage email) {
        javaMailSender.send(email);
    }

    @Override
    public void sendEmailWithCheck(User user, Event event, long[] ticketNumbers, PaymentRequest paymentRequest) {

        MimeMessage simpleMailMessage;
        try {
            simpleMailMessage = createMailWithCheck(user, event, ticketNumbers, paymentRequest);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
        sendEmail(simpleMailMessage);
    }

    @Override
    public void sendEmailWithConfirmationCode(ConfirmationCode confirmationCode, User user) {

        if (confirmationCode != null) {
            confirmationCodeService.delete(confirmationCode);
        }
        confirmationCode = confirmationCodeService.generateConfirmationCode(user);

        MimeMessage simpleMailMessage;
        try {
            simpleMailMessage = createMailWithConfirmationCode(user, confirmationCode);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
        sendEmail(simpleMailMessage);
    }
}