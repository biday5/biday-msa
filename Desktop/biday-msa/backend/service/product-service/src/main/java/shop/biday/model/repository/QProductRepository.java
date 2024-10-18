package shop.biday.model.repository;

import shop.biday.model.domain.ProductModel;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface QProductRepository {
    Map<Long, ProductModel> findAllProduct();

    Map<Long, ProductModel> findAllByProductName(Long id, String name);

    Map<Long, ProductModel> findByProductId(Long id);

    SizeModel findBySizeId(Long id);

    List<ProductDto> findProducts(Long categoryId, Long brandId, String keyword, String color, String order);
}