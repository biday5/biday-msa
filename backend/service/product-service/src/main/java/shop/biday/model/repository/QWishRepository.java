package shop.biday.model.repository;

import java.util.List;

public interface QWishRepository {

    List<?> findByUserId(String userId);

    boolean existsByUserIdAndProductId(String userId, Long productId);

    void deleteWish(String userId, Long productId);
}
