package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.CategoryModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.CategoryEntity;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.service.CategoryService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public ResponseEntity<List<CategoryModel>> findAll() {
        log.info("Finding all categories");
        List<CategoryModel> categories = categoryRepository.findAllCategory();
        return categories.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryModel> findById(Long id) {
        log.info("Finding category by id: {}", id);
        try {
            return Optional.ofNullable(id)
                    .filter(t -> categoryRepository.existsById(id))
                    .flatMap(categoryRepository::findById)
                    .map(categoryEntity -> {
                        CategoryModel categoryModel = CategoryModel.builder()
                                .id(categoryEntity.getId())
                                .name(categoryEntity.getName())
                                .createdAt(categoryEntity.getCreatedAt())
                                .updatedAt(categoryEntity.getUpdatedAt())
                                .build();
                        return new ResponseEntity<>(categoryModel, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("Category not found with id: {}", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            log.error("Internal server error while finding category", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CategoryEntity> save(String userInfoHeader, CategoryModel category) {
        log.info("Saving category started with user: {}", userInfoHeader);
        try {
            return isAdmin(userInfoHeader)
                    .map(t -> {
                        CategoryEntity savedCategory = categoryRepository.save(CategoryEntity.builder()
                                .name(category.getName())
                                .build());
                        log.info("Category saved successfully: {}", savedCategory.getId());
                        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("Save category failed : User does not have permission");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    });
        } catch (Exception e) {
            log.error("Internal server error while saving category", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CategoryEntity> update(String userInfoHeader, CategoryModel category) {
        log.info("Updating category started for id: {}", category.getId());
        try {
            return isAdmin(userInfoHeader)
                    .filter(t -> categoryRepository.existsById(category.getId()))
                    .map(t -> {
                        CategoryEntity updatedCategory = categoryRepository.save(CategoryEntity.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .build());
                        log.info("Category updated successfully: {}", updatedCategory.getId());
                        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("Update Category failed: Category not found or user does not have permission");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    });
        } catch (Exception e) {
            log.error("Internal server error while updating category", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfoHeader, Long id) {
        log.info("Deleting category started for id: {}", id);
        try {
            return isAdmin(userInfoHeader)
                    .map(t -> {
                        if (!categoryRepository.existsById(id)) {
                            log.error("Category not found with id: {}", id);
                            return new ResponseEntity<>("카테고리 삭제 실패: 카테고리를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
                        }

                        categoryRepository.deleteById(id);
                        log.info("Category deleted successfully: {}", id);
                        return new ResponseEntity<>("카테고리 삭제 성공", HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.error("User does not have role ADMIN or Category does not exist");
                        return new ResponseEntity<>("유효하지 않은 사용자: 관리자 권한이 필요합니다", HttpStatus.FORBIDDEN);
                    });
        } catch (Exception e) {
            log.error("Internal server error while deleting category", e);
            return new ResponseEntity<>("서버 오류", HttpStatus.INTERNAL_SERVER_ERROR);
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
