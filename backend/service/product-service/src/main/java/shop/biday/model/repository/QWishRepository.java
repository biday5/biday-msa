package shop.biday.model.repository;

import shop.biday.model.entity.WishEntity;

import java.util.List;

public interface QWishRepository {

    void deleteWish(String userId, Long productId);

    WishEntity findByUserIdAndProductId(String userId, Long productId);

    List<?> findByUserId(String userId);

}
