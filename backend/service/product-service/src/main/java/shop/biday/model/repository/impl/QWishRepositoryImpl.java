package shop.biday.model.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.QWishEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.QWishRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QWishRepositoryImpl implements QWishRepository {

    private final JPAQueryFactory queryFactory;

    QWishEntity wishEntity = QWishEntity.wishEntity;

    @Override
    public void deleteWish(String userId, Long productId) {

        queryFactory.delete(wishEntity)
                .where(wishEntity.userId.eq(userId)
                        .and(wishEntity.product.id.eq(productId)))
                .execute();
    }

    @Override
    public WishEntity findByUserIdAndProductId(String userId, Long productId) {
        queryFactory.select(wishEntity)
                .where(wishEntity.userId.eq(userId)
                        .and(wishEntity.product.id.eq(productId)));

        return null;
    }

    @Override
    public List<?> findByUserId(String userId) {
        return queryFactory.selectFrom(wishEntity)
                .where(wishEntity.userId.eq(userId))
                .fetch();
    }
}