package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.ConfirmationCodeRepository;
import kg.something.events_app_backend.service.ConfirmationCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationCodeServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(ConfirmationCode confirmationCode) {
        repository.delete(confirmationCode);
    }

    @Override
    public ConfirmationCode findByUserAndCode(User user, Integer code) {
        return repository.findByUserAndCode(user, code)
                .orElseThrow(() -> new ResourceNotFoundException("Код подтверждения для пользователя с почтой '%s' не найден".formatted(user.getEmail())));
    }

    @Override
    public ConfirmationCode findConfirmationCodeByUser(User user) {
        return repository.findConfirmationCodeByUser(user);
    }

    @Override
    public ConfirmationCode generateConfirmationCode(User user) {
        Random random = new Random();
        Integer code = random.nextInt(1000, 9999);

        ConfirmationCode confirmationCode = new ConfirmationCode(code, user, LocalDateTime.now().plusMinutes(10));
        repository.save(confirmationCode);

        return confirmationCode;
    }

    @Override
    public void save(ConfirmationCode confirmationCode) {
        repository.save(confirmationCode);
    }
}