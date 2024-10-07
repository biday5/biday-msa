package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.enums.Color;
import shop.biday.model.repository.BrandRepository;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.model.repository.ProductRepository;
import shop.biday.service.ProductService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Map<Long, ProductModel> findAll() {
        log.info("Finding all products");
        return productRepository.findAllProduct();
    }

    @Override
    public List<Map.Entry<Long, ProductModel>> findAllByProductName(Long id) {
        log.info("Finding product by id: {}", id);

        return productRepository.findById(id)
                .map(product -> {
                    String productName = product.getName();
                    log.info("Product found: {} with name: {}", id, productName);

                    Map<Long, ProductModel> map = productRepository.findAllByProductName(id, removeParentheses(productName));
                    return Objects.requireNonNull(map).entrySet().stream().toList();
                })
                .orElseGet(() -> {
                    log.error("Product not found with id: {}", id);
                    return List.of();
                });
    }

    @Override
    public Map<Long, ProductModel> findByProductId(Long id) {
        log.info("Find product by id: {}", id);

        return productRepository.findById(id)
                .map(product -> {
                    log.info("Product found: {}", product);
                    return productRepository.findByProductId(id);
                })
                .orElseGet(() -> {
                    log.error("Product not found with id: {}", id);
                    return null;
                });
    }

    public static String removeParentheses(String productName) {
        int index = productName.indexOf("(");
        return (index != -1) ? productName.substring(0, index) : productName;
    }

    @Override
    public ProductEntity save(String userInfoHeader, ProductModel product) {
        log.info("Saving product started with user: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(t -> {
                    ProductEntity savedProduct = createProductEntity(product);
                    log.info("Product saved successfully: {}", savedProduct.getId());
                    return productRepository.save(savedProduct);
                })
                .orElseThrow(() -> new RuntimeException("Save Product failed: User does not have permission"));
    }

    @Override
    public ProductEntity update(String userInfoHeader, ProductModel product) {
        log.info("Updating product started for id: {}", product.getId());
        return validateUser(userInfoHeader)
                .filter(t -> {
                    boolean exists = productRepository.existsById(product.getId());
                    if (!exists) {
                        log.error("Product not found with id: {}", product.getId());
                    }
                    return exists;
                })
                .map(t -> {
                    ProductEntity updatedProduct = createProductEntity(product);
                    updatedProduct.setId(product.getId());
                    log.info("Product updated successfully: {}", updatedProduct.getId());
                    return productRepository.save(updatedProduct);
                })
                .orElseThrow(() -> new RuntimeException("Update Product failed: Product not found or user does not have permission"));
    }

    @Override
    public String deleteById(String userInfoHeader, Long id) {
        log.info("Deleting product started for id: {}", id);

        return validateUser(userInfoHeader).map(t -> {
            if (!productRepository.existsById(id)) {
                log.error("Product not found with id: {}", id);
                return "상품 삭제 실패: 상품을 찾을 수 없습니다";
            }

            productRepository.deleteById(id);
            log.info("Product deleted successfully: {}", id);
            return "상품 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자: 관리자 권한이 필요합니다";
        });
    }

    @Override
    public List<ProductDto> findByFilter(Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        log.info("Finding products by filter started");
        log.info("Filter parameters - categoryId: {}, brandId: {}, keyword: {}, color: {}, order: {}, lastItemId: {}",
                categoryId, brandId, keyword, color, order, lastItemId);
        return productRepository.findProducts(categoryId, brandId, keyword, color, order, lastItemId);
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserRole())
                .filter(role -> role.equalsIgnoreCase("ROLE_ADMIN"))
                .or(() -> {
                    log.error("User does not have role ADMIN: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }

    private ProductEntity createProductEntity(ProductModel product) {
        log.info("Creating product entity for product: {}", product.getName());
        return ProductEntity.builder()
                .brand(brandRepository.findByName(product.getBrand()))
                .category(categoryRepository.findByName(product.getCategory()))
                .name(product.getName())
                .subName(product.getSubName())
                .productCode(product.getProductCode())
                .price(product.getPrice())
                .color(Color.valueOf(product.getColor()))
                .description(product.getDescription())
                .build();
    }
}
