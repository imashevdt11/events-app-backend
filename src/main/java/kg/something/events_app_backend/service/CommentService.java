package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Comment;

import java.util.UUID;

public interface CommentService {

    void delete(Comment comment);

    Comment findCommentById(UUID id);

    void save(Comment comment);

}
