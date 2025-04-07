package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Comment;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.CommentRepository;
import kg.something.events_app_backend.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    public void save(Comment comment) {
        repository.save(comment);
    }

    public void delete(Comment comment) {
        repository.delete(comment);
    }

    public Comment findCommentById(UUID id) {
        return Optional.ofNullable(repository.findCommentById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий с id '%s' не найден в базе данных".formatted(id)));
    }
}
