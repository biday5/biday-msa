package shop.biday.service;

import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ResponseEntity<Map<Long, ProductModel>> findAll();

    ResponseEntity<List<Map.Entry<Long, ProductModel>>> findAllByProductName(Long id);

    ResponseEntity<Map<Long, ProductModel>> findByProductId(Long id);

    ResponseEntity<List<ProductDto>> findByFilter(Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);

    ResponseEntity<ProductEntity> save(String userInfoHeader, ProductModel product);

    ResponseEntity<ProductEntity> update(String userInfoHeader, ProductModel product);

    ResponseEntity<String> deleteById(String userInfoHeader, Long id);
}
