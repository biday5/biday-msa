package shop.biday.service;

import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<List<CategoryModel>> findAll();

    ResponseEntity<CategoryModel> findById(Long id);

    ResponseEntity<CategoryEntity> save(String userInfoHeader, CategoryModel category);

    ResponseEntity<CategoryEntity> update(String userInfoHeader, CategoryModel category);

    ResponseEntity<String> deleteById(String userInfoHeader, Long id);
}
