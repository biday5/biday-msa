package shop.biday.service;

import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;

import java.util.List;

public interface BrandService {
    ResponseEntity<List<BrandModel>> findAll();

    ResponseEntity<BrandModel> findById(Long id);

    ResponseEntity<BrandEntity> save(String userInfoHeader, BrandModel brand);

    ResponseEntity<BrandEntity> update(String userInfoHeader, BrandModel brand);

    ResponseEntity<String> deleteById(String userInfoHeader, Long id);
}
