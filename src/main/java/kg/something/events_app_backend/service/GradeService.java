package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.User;

public interface GradeService {
    Grade findGradeByEventAndUser(Event event, User user);

    void save(Grade grade);

    void delete(Grade grade);
}
