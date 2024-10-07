package shop.biday.service;

import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> findAll();

    CategoryModel findById(Long id);

    CategoryEntity save(String userInfoHeader, CategoryModel category);

    CategoryEntity update(String userInfoHeader, CategoryModel category);

    String deleteById(String userInfoHeader, Long id);
}
