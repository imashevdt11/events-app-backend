package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.EventGrade;

import java.util.List;

public interface GradeService {
    Grade findGradeByEventAndUser(Event event, User user);

    void save(Grade grade);

    void delete(Grade grade);

    Integer getEventAmountOfLikes(Event event);

    Integer getEventAmountOfDislikes(Event event);

    List<Grade> findGradesByUserAndName(User user, EventGrade eventGrade);
}
