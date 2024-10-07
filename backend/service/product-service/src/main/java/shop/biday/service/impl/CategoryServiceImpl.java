package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<CategoryModel> findAll() {
        log.info("Finding all categories");
        return categoryRepository.findAllCategory();
    }

    @Override
    public CategoryModel findById(Long id) {
        log.info("Finding category by id: {}", id);
        return Optional.ofNullable(id)
                .filter(t -> {
                    boolean exists = categoryRepository.existsById(t);
                    if (!exists) {
                        log.error("Category not found with id: {}", id);
                    }
                    return exists;
                })
                .flatMap(categoryRepository::findById)
                .map(categoryEntity -> CategoryModel.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .createdAt(categoryEntity.getCreatedAt())
                        .updatedAt(categoryEntity.getUpdatedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public CategoryEntity save(String userInfoHeader, CategoryModel category) {
        log.info("Saving category started with user: {}", userInfoHeader);
        return isAdmin(userInfoHeader)
                .map(t -> {
                    CategoryEntity savedCategory = categoryRepository.save(CategoryEntity.builder()
                            .name(category.getName())
                            .build());
                    log.info("Category saved successfully: {}", savedCategory.getId());
                    return savedCategory;
                })
                .orElseThrow(() -> new RuntimeException("Save Category failed: User does not have permission"));
    }

    @Override
    public CategoryEntity update(String userInfoHeader, CategoryModel category) {
        log.info("Updating category started for id: {}", category.getId());
        return isAdmin(userInfoHeader)
                .filter(t -> {
                    boolean exists = categoryRepository.existsById(category.getId());
                    if (!exists) {
                        log.error("Category not found with id: {}", category.getId());
                    }
                    return exists;
                })
                .map(t -> {
                    CategoryEntity updatedCategory = categoryRepository.save(CategoryEntity.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build());
                    log.info("Category updated successfully: {}", updatedCategory.getId());
                    return updatedCategory;
                })
                .orElseThrow(() -> new RuntimeException("Update Category failed: Category not found or user does not have permission"));
    }

    @Override
    public String deleteById(String userInfoHeader, Long id) {
        log.info("Deleting category started for id: {}", id);

        return isAdmin(userInfoHeader).map(t -> {
            if (!categoryRepository.existsById(id)) {
                log.error("Category not found with id: {}", id);
                return "카테고리 삭제 실패: 카테고리를 찾을 수 없습니다";
            }

            categoryRepository.deleteById(id);
            log.info("Category deleted successfully: {}", id);
            return "카테고리 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자: 관리자 권한이 필요합니다";
        });
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
