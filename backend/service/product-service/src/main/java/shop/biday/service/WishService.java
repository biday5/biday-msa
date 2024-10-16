package shop.biday.service;

import java.util.List;

public interface WishService {

    List<?> findByUserId(String userInfoHeader);

    boolean toggleWish(String userInfoHeader, Long productId);

    boolean deleteByWishId(String userInfoHeader, Long wishId);

}
