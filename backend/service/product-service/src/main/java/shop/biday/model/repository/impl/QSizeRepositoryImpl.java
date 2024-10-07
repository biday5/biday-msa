package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.QProductEntity;
import shop.biday.model.entity.QSizeEntity;
import shop.biday.model.repository.QSizeRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QSizeRepositoryImpl implements QSizeRepository {
    private final JPAQueryFactory queryFactory;

    private final QSizeEntity qSize = QSizeEntity.sizeEntity;
    private final QProductEntity qProduct = QProductEntity.productEntity;

    @Override
    public List<SizeModel> findAllByProductId(Long productId) {
        return queryFactory
                .select(Projections.constructor(SizeModel.class,
                        qSize.id,
                        qProduct.name.as("product"),
                        qSize.size.stringValue(),
                        qSize.createdAt,
                        qSize.updatedAt
                ))
                .from(qSize)
                .leftJoin(qSize.product, qProduct)
                .where(qProduct.id.eq(productId))
                .fetch();
    }

}
