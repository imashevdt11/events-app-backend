package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.EventGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {
    Grade findByEventAndUser(Event event, User user);

    Integer countGradesByEventAndName(Event event, EventGrade name);

    List<Grade> findGradesByUserAndName(User user, EventGrade name);
}
