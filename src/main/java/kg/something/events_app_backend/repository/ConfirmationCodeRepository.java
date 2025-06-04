package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByUserAndCode(User user, Integer code);

    ConfirmationCode findConfirmationCodeByUser(User user);
}