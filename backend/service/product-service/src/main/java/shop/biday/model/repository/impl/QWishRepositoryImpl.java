package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.biday.model.dto.ProductResponse;
import shop.biday.model.dto.WishDto;
import shop.biday.model.entity.QProductEntity;
import shop.biday.model.entity.QWishEntity;
import shop.biday.model.repository.QWishRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QWishRepositoryImpl implements QWishRepository {

    private final JPAQueryFactory queryFactory;

    QWishEntity qWish = QWishEntity.wishEntity;
    QProductEntity qProduct = QProductEntity.productEntity;

    @Override
    public List<?> findByUserId(String userId) {
        return queryFactory.select(Projections.constructor(WishDto.class,
                        qWish.id,
                        Projections.constructor(ProductResponse.class,
                                qProduct.id,
                                qProduct.name,
                                qProduct.subName,
                                qProduct.productCode,
                                qProduct.price,
                                qProduct.color.stringValue()),
                        qWish.status,
                        qWish.createdAt,
                        qWish.updatedAt))
                .from(qWish)
                .join(qWish.product, qProduct)
                .where(qWish.userId.eq(userId))
                .fetch();
    }

    @Override
    public boolean existsByUserIdAndProductId(String userId, Long productId) {
        return queryFactory.selectFrom(qWish)
                .where(qWish.userId.eq(userId)
                        .and(qWish.product.id.eq(productId)))
                .fetchOne() != null;
    }

    @Override
    public void deleteWish(String userId, Long productId) {
        queryFactory.delete(qWish)
                .where(qWish.userId.eq(userId)
                        .and(qWish.product.id.eq(productId)))
                .execute();
    }
}