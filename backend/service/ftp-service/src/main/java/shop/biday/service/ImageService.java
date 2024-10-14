package shop.biday.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ResponseEntity<?> getImage(String id);

    ResponseEntity<String> uploadFileByAdmin(String userInfoHeader, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    ResponseEntity<String> uploadFilesByUser(String userInfoHeader, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    ResponseEntity<String> update(String userInfoHeader, List<MultipartFile> multipartFiles, String id);

    ResponseEntity<String> deleteById(String userInfoHeader, String id);
}
