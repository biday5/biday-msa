package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.repository.QSizeRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QSizeRepositoryImpl implements QSizeRepository {
    private final JPAQueryFactory queryFactory;

    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QSizeEntity qSize = QSizeEntity.sizeEntity;
    private final QWishEntity qWish = QWishEntity.wishEntity;

    @Override
    public List<SizeModel> findAllSize() {
        return queryFactory
                .select(createSizeModelProjection())
                .from(qSize)
                .leftJoin(qSize.product, qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .fetch();
    }

    @Override
    public List<SizeModel> findAllByProductId(Long productId) {
        return queryFactory
                .select(createSizeModelProjection())
                .from(qSize)
                .leftJoin(qSize.product, qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .where(qProduct.id.eq(productId))
                .fetch();
    }

    private ConstructorExpression<SizeModel> createSizeModelProjection() {
        return Projections.constructor(SizeModel.class,
                qSize.id,
                createProductDtoProjection(),
                qSize.size.stringValue(),
                qSize.createdAt,
                qSize.updatedAt
        );
    }

    private ConstructorExpression<ProductDto> createProductDtoProjection() {
        return Projections.constructor(ProductDto.class,
                qProduct.id,
                qBrand.name.as("brand"),
                qCategory.name.as("category"),
                qProduct.name,
                qProduct.subName,
                qProduct.productCode,
                qProduct.price,
                qProduct.color.stringValue(),
                qProduct.createdAt,
                qProduct.updatedAt,
                wishCount()
        );
    }

    private JPQLQuery<Long> wishCount() {
        return JPAExpressions.select(qWish.count().coalesce(0L))
                .from(qWish)
                .where(qWish.product.id.eq(qProduct.id));
    }
}
