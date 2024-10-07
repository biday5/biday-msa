package shop.biday.model.repository;

import shop.biday.model.domain.BrandModel;

import java.util.List;

public interface QBrandRepository {
    List<BrandModel> findAllBrand();
}
