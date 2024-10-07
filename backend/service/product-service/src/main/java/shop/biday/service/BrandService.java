package shop.biday.service;

import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;

import java.util.List;

public interface BrandService {
    List<BrandModel> findAll();

    BrandModel findById(Long id);

    BrandEntity save(String userInfoHeader, BrandModel brand);

    BrandEntity update(String userInfoHeader, BrandModel brand);

    String deleteById(String userInfoHeader, Long id);
}
