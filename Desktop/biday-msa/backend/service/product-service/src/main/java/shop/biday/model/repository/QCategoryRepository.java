package shop.biday.model.repository;

import shop.biday.model.domain.CategoryModel;

import java.util.List;

public interface QCategoryRepository {
    List<CategoryModel> findAllCategory();
}
