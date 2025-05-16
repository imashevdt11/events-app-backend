package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.EventGrade;
import kg.something.events_app_backend.repository.GradeRepository;
import kg.something.events_app_backend.service.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository repository;

    public GradeServiceImpl(GradeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(Grade grade) {
        repository.delete(grade);
    }

    @Override
    public Grade findGradeByEventAndUser(Event event, User user) {
        return repository.findByEventAndUser(event, user);
    }

    @Override
    public List<Grade> findGradesByUserAndName(User user, EventGrade eventGrade) {
        return repository.findGradesByUserAndName(user, eventGrade);
    }

    @Override
    public Integer getEventAmountOfDislikes(Event event) {
        return repository.countGradesByEventAndName(event, EventGrade.DISLIKE);
    }

    @Override
    public Integer getEventAmountOfLikes(Event event) {
        return repository.countGradesByEventAndName(event, EventGrade.LIKE);
    }

    @Override
    public void save(Grade grade) {
        repository.save(grade);
    }
}
