package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.SizeEntity;
import shop.biday.model.entity.enums.Size;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.SizeRepository;
import shop.biday.service.SizeService;
import shop.biday.utils.UserInfoUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public ResponseEntity<List<SizeEntity>> findAll() {
        log.info("Finding all sizes");
        List<SizeEntity> sizes = sizeRepository.findAll();
        return sizes.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(sizes);
    }

    @Override
    public ResponseEntity<SizeEntity> findById(Long id) {
        log.info("Finding size by id: {}", id);
        return sizeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("Size not found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @Override
    public ResponseEntity<List<SizeModel>> findAllByProductId(Long productId) {
        log.info("Finding sizes by Product id: {}", productId);
        List<SizeModel> sizes = sizeRepository.findAllByProductId(productId);
        return sizes.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(sizes);
    }

    @Override
    public ResponseEntity<SizeEntity> save(String userInfoHeader, SizeModel size) {
        log.info("Saving size started with user: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(t -> {
                    SizeEntity newSize = createSizeEntity(size);
                    log.info("Size saved successfully: {}", newSize);
                    return ResponseEntity.status(HttpStatus.CREATED).body(sizeRepository.save(newSize));
                })
                .orElseGet(() -> {
                    log.error("Save Size failed: User does not have permission");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                });
    }

    @Override
    public ResponseEntity<SizeEntity> update(String userInfoHeader, SizeModel size) {
        log.info("Updating size started for id: {}", size.getId());
        return validateUser(userInfoHeader)
                .filter(t -> sizeRepository.existsById(size.getId()))
                .map(t -> {
                    SizeEntity updatedSize = createSizeEntity(size);
                    updatedSize.setId(size.getId());
                    log.info("Size updated successfully: {}", updatedSize);
                    return ResponseEntity.ok(sizeRepository.save(updatedSize));
                })
                .orElseGet(() -> {
                    log.error("Update Size failed: Size not found or user does not have permission");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfoHeader, Long id) {
        log.info("Deleting size started for id: {}", id);
        return validateUser(userInfoHeader)
                .map(t -> {
                    if (!sizeRepository.existsById(id)) {
                        log.error("Size not found with id: {}", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사이즈 삭제 실패: 사이즈를 찾을 수 없습니다.");
                    }
                    sizeRepository.deleteById(id);
                    log.info("Size deleted successfully: {}", id);
                    return ResponseEntity.ok("사이즈 삭제 성공");
                }).orElseGet(() -> {
                    log.error("User does not have role ADMIN or does not exist");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 사용자: 관리자 권한이 필요합니다.");
                });
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserRole())
                .filter(t -> t.equalsIgnoreCase("ROLE_ADMIN"))
                .or(() -> {
                    log.error("User does not have role ADMIN: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }

    private SizeEntity createSizeEntity(SizeModel size) {
        return SizeEntity.builder()
                .size(Size.valueOf(size.getSize()))
                .product(productRepository.findByName(size.getSizeProduct()))
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
