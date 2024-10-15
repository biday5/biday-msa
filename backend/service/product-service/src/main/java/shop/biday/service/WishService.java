package shop.biday.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishService {

    List<?> findByUserId(String userInfoHeader);

    boolean toggleWish(String userInfoHeader, Long productId);

    ResponseEntity<String> deleteByWishId(String userInfoHeader, Long wishId);

}
