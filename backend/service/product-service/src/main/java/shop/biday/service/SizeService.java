package shop.biday.service;

import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<SizeEntity> findAll();

    Optional<SizeEntity> findById(Long id);

    List<SizeModel> findAllByProductId(Long productId);

    SizeEntity save(String userInfoHeader, SizeModel size);

    SizeEntity update(String userInfoHeader, SizeModel size);

    String deleteById(String userInfoHeader, Long id);
}
