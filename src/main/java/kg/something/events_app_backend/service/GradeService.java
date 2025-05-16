package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.EventGrade;

import java.util.List;

public interface GradeService {

    void delete(Grade grade);

    Grade findGradeByEventAndUser(Event event, User user);

    List<Grade> findGradesByUserAndName(User user, EventGrade eventGrade);

    Integer getEventAmountOfDislikes(Event event);

    Integer getEventAmountOfLikes(Event event);

    void save(Grade grade);
}
