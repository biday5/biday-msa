package shop.biday.model.repository;

import shop.biday.model.domain.SizeModel;

import java.util.List;

public interface QSizeRepository {
    List<SizeModel> findAllByProductId(Long productId);
}
