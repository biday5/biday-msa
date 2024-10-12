package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QProductRepository;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QProductRepositoryImpl implements QProductRepository {
    private final JPAQueryFactory queryFactory;

    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QSizeEntity qSize = QSizeEntity.sizeEntity;
    private final QWishEntity qWish = QWishEntity.wishEntity;

    @Override
    public Map<Long, ProductModel> findAllProduct() {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qSize).on(qSize.product.id.eq(qProduct.id))
                .orderBy(qProduct.id.asc())
                .transform(groupBy(qProduct.id).as(createProductModelProjection()));
    }

    private ConstructorExpression<ProductModel> createProductModelProjection() {
        return Projections.constructor(ProductModel.class,
                qProduct.id,
                qBrand.name.as("brand"),
                qCategory.name.as("category"),
                qProduct.name,
                qProduct.subName,
                qProduct.productCode,
                qProduct.price,
                qProduct.color.stringValue(),
                qProduct.description,
                qProduct.createdAt,
                qProduct.updatedAt,
                wishCount(),
                set(createDefaultSizeProjection())
        );
    }

    @Override
    public List<ProductDto> findProducts(Long categoryId, Long brandId, String keyword, String color, String order) {
        return createBaseQuery(queryFactory, categoryId, brandId, keyword, color, order)
                .fetch();
    }

    @Override
    public Map<Long, ProductModel> findAllByProductName(Long id, String name) {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qSize).on(qSize.product.id.eq(qProduct.id))
                .where(qProduct.id.eq(id).or(qProduct.name.containsIgnoreCase(name)))
                .orderBy(qSize.id.asc())
                .transform(groupBy(qProduct.id).as(createProductModelProjection()));
    }

    @Override
    public Map<Long, ProductModel> findByProductId(Long id) {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qSize).on(qSize.product.id.eq(qProduct.id))
                .where(qProduct.id.eq(id))
                .orderBy(qSize.id.asc())
                .transform(groupBy(qProduct.id).as(createProductModelProjection()));
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
                wishCount()
        );
    }

    private JPQLQuery<Long> wishCount() {
        return JPAExpressions.select(qWish.count().coalesce(0L))
                .from(qWish)
                .where(qWish.product.id.eq(qProduct.id));
    }

    private ConstructorExpression<SizeModel> createDefaultSizeProjection() {
        return Projections.constructor(SizeModel.class,
                qSize.id,
                qProduct.name.as("sizeProduct"),
                qSize.size.stringValue(),
                qSize.createdAt,
                qSize.updatedAt
        );
    }

    private JPQLQuery<ProductDto> createBaseQuery(JPAQueryFactory queryFactory, Long categoryId, Long brandId, String keyword,
                                                  String color, String order) {
        JPQLQuery<ProductDto> query = queryFactory
                .select(createProductDtoProjection())
                .from(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qProduct.sizes, qSize)
                .where(
                        findByBrand(brandId),
                        findByCategory(categoryId),
                        findByColor(color),
                        findByKeyword(keyword)
                );

        findByOrdering(query, order);

        return query
                .groupBy(qProduct.id, qBrand.name, qCategory.name, qProduct.name, qProduct.subName, qProduct.productCode, qProduct.price, qProduct.color.stringValue());
    }

    private BooleanExpression findByCategory(Long categoryId) {
        return categoryId != null ? qCategory.id.eq(categoryId) : null;
    }

    private BooleanExpression findByBrand(Long brandId) {
        return brandId != null ? qBrand.id.eq(brandId) : null;
    }

    private BooleanExpression findByColor(String color) {
        return color != null ? qProduct.color.stringValue().containsIgnoreCase(color) : null;
    }

    private BooleanExpression findByKeyword(String keyword) {
        return keyword != null ? qProduct.name.containsIgnoreCase(keyword)
                .or(qProduct.subName.containsIgnoreCase(keyword))
                .or(qProduct.productCode.containsIgnoreCase(keyword))
                .or(qProduct.color.stringValue().containsIgnoreCase(keyword))
                .or(qCategory.name.containsIgnoreCase(keyword))
                .or(qBrand.name.containsIgnoreCase(keyword)) : null;
    }

    private void findByOrdering(JPQLQuery<ProductDto> query, String order) {
        switch (order) {
            case "가격 낮은 순" -> query.orderBy(qProduct.price.asc());
            case "가격 높은 순" -> query.orderBy(qProduct.price.desc());
            case "위시 적은 순" -> query.orderBy(qWish.count().asc());
            case "위시 많은 순" -> query.orderBy(qWish.count().desc());
            default -> query.orderBy(qProduct.id.asc());
        }
    }

    private Long fetchProductByPrice(Long id) {
        return queryFactory
                .select(qProduct.price.coalesce(0L))
                .from(qProduct)
                .where(qProduct.id.eq(id))
                .fetchOne();
    }

    private Long fetchWishByCount(Long id) {
        return queryFactory
                .select(qWish.count().coalesce(0L))
                .from(qProduct)
                .where(qWish.product.id.eq(id))
                .fetchOne();
    }
}