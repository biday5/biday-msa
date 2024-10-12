package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.net.http.HttpResponse;
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
    public ResponseEntity<Map<Long, ProductModel>> findAll() {
        log.info("Finding all products");
        Map<Long, ProductModel> products = productRepository.findAllProduct();
        return products.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Map.Entry<Long, ProductModel>>> findAllByProductName(Long id) {
        log.info("Finding product by id: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    String productName = product.getName();
                    log.debug("Product found: {} with name: {}", id, productName);

                    Map<Long, ProductModel> map = productRepository.findAllByProductName(id, removeParentheses(productName));
                    return new ResponseEntity<>(Objects.requireNonNull(map).entrySet().stream().toList(), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public ResponseEntity<Map<Long, ProductModel>> findByProductId(Long id) {
        log.info("Find product by id: {}", id);

        return productRepository.findById(id)
                .map(product -> {
                    log.debug("Product found: {}", product);
                    return new ResponseEntity<>(productRepository.findByProductId(id), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    public static String removeParentheses(String productName) {
        int index = productName.indexOf("(");
        return (index != -1) ? productName.substring(0, index) : productName;
    }

    @Override
    public ResponseEntity<ProductEntity> save(String userInfoHeader, ProductModel product) {
        log.info("Saving product started with user: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(t -> {
                    ProductEntity savedProduct = createProductEntity(product);
                    log.debug("Product saved successfully: {}", savedProduct.getId());
                    return new ResponseEntity<>(productRepository.save(savedProduct),HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.error("Save Product failed: User does not have permission");
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                });
    }

    @Override
    public ResponseEntity<ProductEntity> update(String userInfoHeader, ProductModel product) {
        log.info("Updating product started for id: {}", product.getId());
        return validateUser(userInfoHeader)
                .filter(t -> productRepository.existsById(product.getId()))
                .map(t -> {
                    ProductEntity updatedProduct = createProductEntity(product);
                    updatedProduct.setId(product.getId());
                    log.debug("Product updated successfully: {}", updatedProduct.getId());
                    return new ResponseEntity<>(productRepository.save(updatedProduct), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.error("Update Product failed: Product not found or user does not have permission");
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                });
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfoHeader, Long id) {
        log.info("Deleting product started for id: {}", id);
        return validateUser(userInfoHeader)
                .filter(t -> productRepository.existsById(id))
                .map(t -> {
                    productRepository.deleteById(id);
                    log.debug("Product deleted successfully: {}", id);
                    return new ResponseEntity<>("상품 삭제 성공", HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.error("User does not have role ADMIN or does not exist");
                    return new ResponseEntity<>("유효하지 않은 사용자: 관리자 권한이 필요합니다", HttpStatus.FORBIDDEN);
                });
    }

    @Override
    public ResponseEntity<List<ProductDto>> findByFilter(Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        log.info("Finding products by filter started");
        log.info("Filter parameters - categoryId: {}, brandId: {}, keyword: {}, color: {}, order: {}, lastItemId: {}",
                categoryId, brandId, keyword, color, order, lastItemId);
        List<ProductDto> products = productRepository.findProducts(categoryId, brandId, keyword, color, order, lastItemId);
        return products.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(products, HttpStatus.OK);
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
