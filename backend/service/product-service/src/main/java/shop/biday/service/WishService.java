package shop.biday.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishService {
    List<?> findByUserId(String userInfoHeader);

    boolean toggleWish(String userInfoHeader, Long productId);

    void deleteWish(String userInfoHeader, Long productId);

    void insertWish(String userInfoHeader, Long productId);

    boolean isWish(String userInfoHeader, Long productId);

    ResponseEntity<String> deleteByWishId(String userInfoHeader, Long wishId);

}
