package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.service.BrandService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public ResponseEntity<List<BrandModel>> findAll() {
        log.info("Find all brands");
        List<BrandModel> brands = brandRepository.findAllBrand();
        return brands.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrandModel> findById(Long id) {
        log.info("Find brand by id: {}", id);
        try {
            return Optional.ofNullable(id)
                    .filter(t -> brandRepository.existsById(id))
                    .flatMap(brandRepository::findById)
                    .map(brandEntity -> BrandModel.builder()
                            .id(brandEntity.getId())
                            .name(brandEntity.getName())
                            .createdAt(brandEntity.getCreatedAt())
                            .updatedAt(brandEntity.getUpdatedAt())
                            .build())
                    .map(brandModel -> new ResponseEntity<>(brandModel, HttpStatus.OK))
                    .orElseGet(() -> {
                        log.error("Not found brand with id: {}", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            log.error("Internal server error while retrieving brand with id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BrandEntity> save(String userInfoHeader, BrandModel brand) {
        log.info("Save Brand started with user: {}", userInfoHeader);
        try {
            return isAdmin(userInfoHeader)
                    .map(t -> {
                        BrandEntity savedBrand = brandRepository.save(BrandEntity.builder()
                                .name(brand.getName())
                                .build());
                        log.info("Brand saved successfully: {}", savedBrand.getId());
                        return new ResponseEntity<>(savedBrand, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("Save brand failed: User does not have permission");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    });
        } catch (Exception e) {
            log.error("Internal server error while saving brand", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BrandEntity> update(String userInfoHeader, BrandModel brand) {
        log.info("Update Brand started for id: {}", brand.getId());
        try {
            return isAdmin(userInfoHeader)
                    .filter(t -> brandRepository.existsById(brand.getId()))
                    .map(t -> {
                        BrandEntity updatedBrand = brandRepository.save(BrandEntity.builder()
                                .id(brand.getId())
                                .name(brand.getName())
                                .build());
                        log.info("Brand updated successfully: {}", updatedBrand.getId());
                        return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        if (!brandRepository.existsById(brand.getId())) {
                            log.error("Update Brand failed: Brand not found for id: {}", brand.getId());
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        } else {
                            log.error("Update Brand failed: User does not have permission");
                            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                        }
                    });
        } catch (Exception e) {
            log.error("Internal server error while updating brand", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfoHeader, Long id) {
        log.info("Delete Brand started for id: {}", id);
        try {
            return isAdmin(userInfoHeader)
                    .map(t -> {
                        if (!brandRepository.existsById(id)) {
                            log.error("Not found brand with id: {}", id);
                            return new ResponseEntity<>("브랜드를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
                        }

                        brandRepository.deleteById(id);
                        log.info("Brand deleted successfully: {}", id);
                        return new ResponseEntity<>("브랜드 삭제 성공", HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("User does not have role ADMIN or Brand does not exist");
                        return new ResponseEntity<>("유효하지 않은 사용자: 관리자 권한이 필요합니다", HttpStatus.FORBIDDEN);
                    });
        } catch (Exception e) {
            log.error("Internal server error while deleting brand", e);
            return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<String> isAdmin(String userInfoHeader) {
        log.info("Validate User role: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.of(userInfoModel.getUserRole())
                .filter(t -> t.equalsIgnoreCase("ROLE_ADMIN"))
                .or(() -> {
                    log.error("User does not have role ADMIN: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }
}
