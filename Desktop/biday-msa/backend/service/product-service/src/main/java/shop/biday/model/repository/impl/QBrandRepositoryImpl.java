package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.QBrandEntity;
import shop.biday.model.repository.QBrandRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QBrandRepositoryImpl implements QBrandRepository {
    private final JPAQueryFactory queryFactory;

    private final QBrandEntity qBrand = QBrandEntity.brandEntity;

    @Override
    public List<BrandModel> findAllBrand() {
        return queryFactory
                .select(Projections.constructor(BrandModel.class,
                        qBrand.id,
                        qBrand.name,
                        qBrand.createdAt,
                        qBrand.updatedAt))
                .from(qBrand)
                .orderBy(qBrand.id.asc())
                .fetch();
    }
}
