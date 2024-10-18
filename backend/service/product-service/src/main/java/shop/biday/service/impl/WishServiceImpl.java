package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return wishRepository.findByUserId(getUserInfoModel(userInfoHeader).getUserId());
    }

    @Override
    @Transactional
    public boolean toggleWish(String userInfoHeader, Long productId) {
        log.info("Toggling wish for userInfo: {} and productId: {}", userInfoHeader, productId);
        String userId = getUserInfoModel(userInfoHeader).getUserId();
        if (existsWish(userId, productId)) {
            return deleteWishAndReturnFalse(userId, productId);
        }

        return insertWishAndReturnTrue(userId, productId);
    }

    private boolean deleteWishAndReturnFalse(String userId, Long productId) {
        log.info("Removing wish for userId: {} and productId: {}", userId, productId);
        wishRepository.deleteWish(userId, productId);
        return false;
    }

    private boolean insertWishAndReturnTrue(String userId, Long productId) {
        log.info("Adding wish for userId: {} and productId: {}", userId, productId);
        wishRepository.save(WishEntity.builder().userId(userId)
                .product(ProductEntity.builder().id(productId).build()).build());
        return true;
    }

    @Override
    public boolean deleteByWishId(String userInfoHeader, Long id) {
        if (!wishRepository.existsById(id)) {
            return false;
        }

        WishEntity wish = wishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
        if (!getUserInfoModel(userInfoHeader).getUserId().equals(wish.getUserId())) {
            return false;
        }

        wishRepository.deleteById(id);
        return true;
    }

    private boolean existsWish(String userId, Long productId) {
        return wishRepository.existsByUserIdAndProductId(userId, productId);
    }

    private UserInfoModel getUserInfoModel(String userInfoHeader) {
        return userInfoUtils.extractUserInfo(userInfoHeader);
    }
}
