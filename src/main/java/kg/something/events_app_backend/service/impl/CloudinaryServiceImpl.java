package kg.something.events_app_backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicId(imageUrl);
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public String extractPublicId(String imageUrl) {
        String withoutProtocol = imageUrl.replaceFirst("https?://[^/]+/", "");
        String withoutPath = withoutProtocol.substring(0, withoutProtocol.lastIndexOf("."));
        return withoutPath.substring(withoutPath.lastIndexOf("/") + 1);
    }

    @Override
    public Image saveImage(MultipartFile image) {
        return new Image(uploadImage(image));
    }

    @Override
    public String uploadImage(MultipartFile file){
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) uploadResult.get("secure_url");
    }
}
