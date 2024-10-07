package shop.biday.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    String uploadFileByAdmin(String userInfoHeader, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    String uploadFilesByUser(String userInfoHeader, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    String update(String userInfoHeader, List<MultipartFile> multipartFiles, String id);

    String deleteById(String userInfoHeader, String id);

    ResponseEntity<byte[]> getImage(String id);

//    Optional<ImageDocument> findById(String id);
//
//    ImageModel findByTypeAndUploadPath(String type, String uploadPath);
//
//    ImageModel findByOriginalNameAndType(String name, String type);
//
//    ImageModel findByOriginalNameAndTypeAndReferencedId(String type, String name, Long referencedId);
//
//    ImageModel findByTypeAndReferencedIdAndUploadPath(String type, String referencedId, String uploadPath);
//
//    List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId);
}
