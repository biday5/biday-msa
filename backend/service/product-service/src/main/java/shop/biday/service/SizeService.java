package shop.biday.service;

import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;

import java.util.List;

public interface SizeService {
    ResponseEntity<List<SizeEntity>> findAll();

    ResponseEntity<SizeEntity> findById(Long id);

    ResponseEntity<List<SizeModel>> findAllByProductId(Long productId);

    ResponseEntity<SizeEntity> save(String userInfoHeader, SizeModel size);

    ResponseEntity<SizeEntity> update(String userInfoHeader, SizeModel size);

    ResponseEntity<String> deleteById(String userInfoHeader, Long id);
}
