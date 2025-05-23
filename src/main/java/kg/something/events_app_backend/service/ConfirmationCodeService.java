package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.User;

public interface ConfirmationCodeService {

    void delete(ConfirmationCode confirmationCode);

    ConfirmationCode findByUserAndCode(User user, Integer code);

    ConfirmationCode findConfirmationCodeByUser(User user);

    ConfirmationCode generateConfirmationCode(User user);

    void save(ConfirmationCode confirmationCode);
}