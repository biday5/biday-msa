package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public List<?> findByUserId(String userInfoHeader) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return wishRepository.findByUserId(userInfoModel.getUserId());
    }

    @Override
    public boolean toggleWish(String userInfoHeader, Long productId) {
        log.info("Toggling wish for userInfo: {} and productId: {}", userInfoHeader, productId);
        String userId = userInfoUtils.extractUserInfo(userInfoHeader).getUserId();
        return isWish(userId, productId)
                ? deleteWishAndReturnFalse(userId, productId)
                : insertWishAndReturnTrue(userId, productId);
    }

    private boolean deleteWishAndReturnFalse(String userId, Long productId) {
        log.info("Removing wish for userId: {} and productId: {}", userId, productId);
        deleteWish(userId, productId);
        return false;
    }

    private boolean insertWishAndReturnTrue(String userId, Long productId) {
        log.info("Adding wish for userId: {} and productId: {}", userId, productId);
        insertWish(userId, productId);
        return true;
    }

    @Override
    public void deleteWish(String userId, Long productId) {
        log.info("Deleting wish for userId: {} and productId: {}", userId, productId);
        wishRepository.deleteWish(userId, productId);
    }

    @Override
    public void insertWish(String userId, Long productId) {
        log.info("Inserting wish for userId: {} and productId: {}", userId, productId);
        wishRepository.save(WishEntity.builder()
                .userId(userId)
                .product(ProductEntity.builder().id(productId).build())
                .build());
    }

    @Override
    public boolean isWish(String userId, Long productId) {
        log.info("Checking if wish exists for userId: {} and productId: {}", userId, productId);
        return wishRepository.findByUserIdAndProductId(userId, productId) != null;
    }

    @Override
    public ResponseEntity<String> deleteByWishId(String userInfoHeader, Long wishId) {
        return wishRepository.findById(wishId)
                .filter(wish -> wish.getUserId().equals(userInfoUtils.extractUserInfo(userInfoHeader).getUserId()))
                .map(wish -> {
                    wishRepository.deleteById(wishId);
                    return ResponseEntity.ok("위시 삭제 성공");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 wishId: " + wishId));
    }
}