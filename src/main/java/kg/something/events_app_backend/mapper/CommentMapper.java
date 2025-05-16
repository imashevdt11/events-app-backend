package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.CommentDto;
import kg.something.events_app_backend.entity.Comment;
import kg.something.events_app_backend.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        Image image = comment.getUser().getImage();
        return new CommentDto(
                comment.getUser().getId(),
                image == null ? null : image.getUrl(),
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getId(),
                comment.getComment(),
                comment.getCreatedAt()
        );
    }
}
