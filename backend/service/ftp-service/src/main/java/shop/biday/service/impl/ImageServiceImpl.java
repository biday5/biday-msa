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
import software.amazon.awssdk.services.s3.model.*;

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
                .map(validRole -> {
                    if (multipartFiles.size() > 3) {
                        throw new IllegalArgumentException("파일은 최대 3장까지만 업로드할 수 있습니다.");
                    }
                    return uploadFiles(multipartFiles, filePath, type, referencedId);
                })
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the role is invalid."));
    }

    @Override
    public ResponseEntity<String> uploadFilesByUser(String role, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Images upload By User started");

        return validateRole(role, "ROLE_SELLER", "ROLE_USER")
                .map(validRole -> {
                    if (multipartFiles.size() > 3) {
                        throw new IllegalArgumentException("파일은 최대 3장까지만 업로드할 수 있습니다.");
                    }
                    return uploadFiles(multipartFiles, filePath, type, referencedId);
                })
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the role is invalid."));
    }

    private ResponseEntity<String> uploadFiles(List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        if (multipartFiles.isEmpty()) {
            log.error("File list is empty");
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        boolean allFilesUploaded = true;

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                allFilesUploaded = false;
            } else {
                allFilesUploaded &= handleFileUpload(multipartFile, filePath, type, referencedId);
            }
        }

        return allFilesUploaded ? ResponseEntity.ok("success") : ResponseEntity.status(500).body("fail");
    }

    private boolean handleFileUpload(MultipartFile multipartFile, String filePath, String type, Long referencedId) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = uploadToS3(multipartFile, filePath, uploadFileName);

        if (uploadFileUrl != null) {
            ImageDocument image = saveImageDocument(originalFileName, uploadFileName, filePath, uploadFileUrl, type, referencedId);
            log.debug("image {} : {}", originalFileName, image);
            return true; // 업로드 성공
        } else {
            deleteImageFromS3(filePath, uploadFileName);
            log.error("Image saved To Mongo Failed : {}", originalFileName);
            return false; // 업로드 실패
        }
    }

    private String uploadToS3(MultipartFile file, String filePath, String uploadFileName) {
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
            return null;
        }
    }

    private ImageDocument saveImageDocument(String originalFileName, String uploadFileName, String filePath, String uploadFileUrl, String type, Long referencedId) {
        log.info("Saving image {} to Mongo: {}", originalFileName, uploadFileName);
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
        return image;
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
                .map(image -> ResponseEntity.ok(updateImage(image, multipartFiles)))
                .orElseGet(() -> {
                    log.error("Image not found: {}", id);
                    return ResponseEntity.status(404).body("이미지 찾을 수 없습니다.");
                });
    }

    private String updateImage(ImageDocument image, List<MultipartFile> multipartFiles) {
        if (multipartFiles.isEmpty()) {
            log.error("File is empty");
            return "fail";
        }

        boolean isSuccess = true;

        for (MultipartFile file : multipartFiles) {
            String originalFileName = file.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = uploadToS3(file, image.getUploadPath(), uploadFileName);

            if (uploadFileUrl != null) {
                image.setOriginalName(originalFileName);
                image.setUploadName(uploadFileName);
                image.setUploadUrl(uploadFileUrl);
                image.setUpdatedAt(LocalDateTime.now());

                imageRepository.save(image);
                log.debug("Image updated in Mongo: {}", image);
            } else {
                isSuccess = false;
                log.error("Failed to upload file: {}", originalFileName);
            }
        }

        return isSuccess ? "success" : "fail";
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
    public ResponseEntity<String> deleteById(String id) {
        return imageRepository.findById(id)
                .map(image -> {
                    deleteImageFromS3(image.getUploadPath(), image.getUploadName());
                    imageRepository.delete(image);
                    return ResponseEntity.ok("success");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("이미지 찾을 수 없습니다."));
    }

    private void deleteImageFromS3(String filePath, String uploadName) {
        String keyName = filePath + "/" + uploadName;
        amazonS3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build());
        log.info("Deleted file from S3: {}/{}", bucketName, keyName);
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