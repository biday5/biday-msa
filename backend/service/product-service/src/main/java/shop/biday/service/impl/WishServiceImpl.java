package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    @Override
    public boolean toggleWish(String userId, Long productId) {
        log.info("Toggling wish for userId: {} and productId: {}", userId, productId);
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
    public boolean isWish(String email, Long productId) {
        log.info("Checking if wish exists for email: {} and productId: {}", email, productId);
        return wishRepository.findByEmailAndProductId(email, productId) != null;
    }
}