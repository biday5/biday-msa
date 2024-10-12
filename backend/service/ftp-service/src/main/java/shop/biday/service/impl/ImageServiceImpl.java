package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.repository.ImageRepository;
import shop.biday.service.ImageService;
import shop.biday.utils.UserInfoUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final S3Client amazonS3Client;
    private final UserInfoUtils userInfoUtils;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Override
    public ResponseEntity<?> getImage(String id) {
        log.info("Get Image: {}", id);
        return imageRepository.findById(id)
                .map(image -> {
                    try {
                        return fetchImageFromS3(image, "Image Name: {}");
                    } catch (IOException e) {
                        log.error("Error fetching image: {}", e.getMessage());
                        return ResponseEntity.status(500).body(null); // Internal Server Error
                    }
                })
                .orElseGet(() -> {
                    log.error("Image not found: {}", id);
                    try {
                        return fetchErrorImage();
                    } catch (IOException e) {
                        log.error("Error fetching error image: {}", e.getMessage());
                        return ResponseEntity.status(500).body(null); // Internal Server Error
                    }
                });
    }

    @Override
    public ResponseEntity<String> uploadFileByAdmin(String userInfoHeader, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Image upload By Admin started");
        return validateRole(userInfoHeader, "ROLE_ADMIN")
                .map(validRole -> uploadFiles(multipartFiles, filePath, type, referencedId))
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the role is invalid."));
    }

    @Override
    public ResponseEntity<String> uploadFilesByUser(String role, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Images upload By User started");
        return validateRole(role, "ROLE_SELLER", "ROLE_USER")
                .map(validRole -> uploadFiles(multipartFiles, filePath, type, referencedId))
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the role is invalid."));
    }

    private ResponseEntity<String> uploadFiles(List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        if (multipartFiles.isEmpty()) {
            log.error("File list is empty");
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        StringBuilder resultMessage = new StringBuilder();
        multipartFiles.forEach(multipartFile -> {
            if (multipartFile.isEmpty()) {
                resultMessage.append("파일이 비어 있습니다.\n");
            } else {
                handleFileUpload(multipartFile, filePath, type, referencedId, resultMessage);
            }
        });

        return ResponseEntity.ok(resultMessage.toString());
    }

    private void handleFileUpload(MultipartFile multipartFile, String filePath, String type, Long referencedId, StringBuilder resultMessage) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = uploadToS3(multipartFile, filePath, uploadFileName, resultMessage);

        if (uploadFileUrl != null) {
            saveImageDocument(originalFileName, uploadFileName, filePath, uploadFileUrl, type, referencedId);
            resultMessage.append("파일 업로드 성공: ").append(originalFileName).append("\n");
        }
    }

    private String uploadToS3(MultipartFile file, String filePath, String uploadFileName, StringBuilder resultMessage) {
        try (InputStream inputStream = file.getInputStream()) {
            String keyName = filePath + "/" + uploadFileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentLength(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            amazonS3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            log.info("File uploaded to S3: {}/{}", bucketName, keyName);
            return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            resultMessage.append("파일 업로드 실패: ").append(file.getOriginalFilename()).append("\n");
            return null;
        }
    }

    private void saveImageDocument(String originalFileName, String uploadFileName, String filePath, String uploadFileUrl, String type, Long referencedId) {
        ImageDocument image = ImageDocument.builder()
                .originalName(originalFileName)
                .uploadName(uploadFileName)
                .uploadPath(filePath)
                .uploadUrl(uploadFileUrl)
                .type(type)
                .referencedId(referencedId)
                .createdAt(LocalDateTime.now())
                .build();
        imageRepository.save(image);
        log.debug("Image saved to Mongo: {}", image);
    }

    public String getUuidFileName(String fileName) {
        String ext = getFileExtension(fileName);
        return UUID.randomUUID() + "." + ext;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) ? "" : fileName.substring(lastDotIndex + 1);
    }

    @Override
    public ResponseEntity<String> update(String role, List<MultipartFile> multipartFiles, String id) {
        log.info("Image update started for ID: {}", id);
        return imageRepository.findById(id)
                .map(image -> {
                    String updateResult = updateImage(image, multipartFiles);
                    return ResponseEntity.ok(updateResult);
                })
                .orElseGet(() -> {
                    log.error("Image not found: {}", id);
                    return ResponseEntity.status(404).body("이미지 찾을 수 없습니다.");
                });
    }

    private String updateImage(ImageDocument image, List<MultipartFile> multipartFiles) {
        if (multipartFiles.isEmpty()) {
            log.error("File is empty");
            return "업로드할 파일이 비어있습니다.";
        }

        StringBuilder resultMessage = new StringBuilder();
        multipartFiles.forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = uploadToS3(file, image.getUploadPath(), uploadFileName, resultMessage);

            if (uploadFileUrl != null) {
                image.setOriginalName(originalFileName);
                image.setUploadName(uploadFileName);
                image.setUploadUrl(uploadFileUrl);
                image.setUpdatedAt(LocalDateTime.now());

                imageRepository.save(image);
                log.debug("Image updated in Mongo: {}", image);
                resultMessage.append("파일 업데이트 성공: ").append(originalFileName).append("\n");
            }
        });

        return resultMessage.toString();
    }

    private ResponseEntity<byte[]> fetchImageFromS3(ImageDocument image, String logMessage) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(image.getUploadPath() + "/" + image.getUploadName())
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = amazonS3Client.getObject(getObjectRequest)) {
            log.debug(logMessage, image.getOriginalName());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(IOUtils.toByteArray(s3Object));
        }
    }

    private ResponseEntity<byte[]> fetchErrorImage() throws IOException {
        ImageModel errorImage = imageRepository.findByTypeAndUploadPath("에러", "error");
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(errorImage.getUploadPath() + "/" + errorImage.getUploadName())
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = amazonS3Client.getObject(getObjectRequest)) {
            log.debug("Fetching error image from S3");
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(IOUtils.toByteArray(s3Object));
        }
    }

    @Override
    public ResponseEntity<String> deleteById(String role, String id) {
        log.info("Image delete start.");
        return Optional.of(id)
                .filter(imageRepository::existsById)
                .map(existingId -> {
                    imageRepository.deleteById(existingId);
                    log.debug("Image deleted from Mongo: {}", existingId);
                    return ResponseEntity.ok("이미지 삭제 성공");
                })
                .orElse(ResponseEntity.status(404).body("이미지 삭제 실패"));
    }

    private Optional<String> validateRole(String userInfoHeader, String... validRoles) {
        log.info("Validate role started for user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.of(userInfoModel.getUserRole())
                .filter(r -> Arrays.stream(validRoles).anyMatch(validRole -> validRole.equalsIgnoreCase(r)))
                .or(() -> {
                    log.error("User does not have a valid role: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }
}


//    @Override
//    public Optional<ImageDocument> findById(String id) {
//        log.info("Find Image By id: {}", id);
//        return imageRepository.findById(id);
//    }
//
//    @Override
//    public ImageModel findByTypeAndUploadPath(String type, String uploadPath) {
//        log.info("Find Image by Type: {}", type);
//        return imageRepository.findByType(type);
//    }
//
//    @Override
//    public ImageModel findByOriginalNameAndType(String name, String type) {
//        log.info("Find Image by Name: {}, Type: {}", name + ".jpg", type);
//        return imageRepository.findByOriginalNameAndType(name + ".jpg", type);
//    }
//
//    @Override
//    public ImageModel findByOriginalNameAndTypeAndReferencedId(String name, String type, Long referencedId) {
//        log.info("Find Image by Name: {} Type: {} ReferencedId: {}", name + ".jpg", type, referencedId);
//        return imageRepository.findByOriginalNameAndTypeAndReferencedId(name + ".jpg", type, referencedId);
//    }
//
//    @Override
//    public ImageModel findByTypeAndReferencedIdAndUploadPath(String type, String referencedId, String uploadPath) {
//        log.info("Find Image by Type: {} ReferencedId: {} UploadPath: {}", type, referencedId, uploadPath);
//        return imageRepository.findByTypeAndReferencedIdAndUploadPath(type, referencedId, uploadPath);
//    }
//
//    @Override
//    public List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId) {
//        log.info("Find Image List by Type: {}, ReferencedId: {}", type, referencedId);
//        return imageRepository.findByTypeAndReferencedId(type, referencedId);
//    }
