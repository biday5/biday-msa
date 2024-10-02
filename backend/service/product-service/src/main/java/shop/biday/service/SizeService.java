package shop.biday.service;

import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<SizeEntity> findAll();

    Optional<SizeEntity> findById(Long id);

    List<SizeModel> findAllByProductId(Long productId);

    SizeEntity save(String role, SizeModel size);

    SizeEntity update(String role, SizeModel size);

    String deleteById(String role, Long id);
}