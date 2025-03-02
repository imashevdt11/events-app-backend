package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    void deleteImage(String imageUrl) throws IOException;

    String extractPublicId(String imageUrl);

    Image saveImage(MultipartFile image);

    String uploadImage(MultipartFile file) throws IOException;

}
