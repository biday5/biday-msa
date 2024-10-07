package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.QAuctionEntity;
import shop.biday.model.entity.QAwardEntity;
import shop.biday.model.repository.QAwardRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QAwardRepositoryImpl implements QAwardRepository {
    private final JPAQueryFactory queryFactory;

    private final QAwardEntity qAward = QAwardEntity.awardEntity;
    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;

    @Override
    public AwardModel findByAwardId(Long id) {
        return queryFactory
                .select(createAwardModelProjection())
                .from(qAward)
                .leftJoin(qAward.auction, qAuction)
                .where(qAward.id.eq(id))
                .fetchOne();
    }

    @Override
    public Slice<AwardModel> findByUser(String userId, String period, LocalDateTime cursor, Pageable pageable) {
        LocalDateTime startDate = switch (period) {
            case "3개월" -> LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
            case "6개월" -> LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
            case "12개월" -> LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
            case "전체보기" -> null;
            default -> throw new IllegalArgumentException("Invalid period specified");
        };

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qAward.bidedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qAward.bidedAt.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<AwardModel> auctions = queryFactory
                .select(createAwardModelProjection())
                .from(qAward)
                .leftJoin(qAward.auction, qAuction)
                .where(qAward.userId.contains(userId)
                        .and(datePredicate)
                        .and(cursorPredicate))
                .orderBy(qAward.bidedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }

        return new SliceImpl<>(auctions, pageable, hasNext);
    }

    private ConstructorExpression<AwardModel> createAwardModelProjection() {
        return Projections.constructor(AwardModel.class,
                qAward.id,
                createAuctionDtoProjection(),
                qAward.userId,
                qAward.bidedAt,
                qAward.currentBid,
                qAward.count);
    }

    private ConstructorExpression<AuctionDto> createAuctionDtoProjection() {
        return Projections.constructor(AuctionDto.class,
                qAuction.id,
                qAuction.userId,
                qAuction.sizeId,
                qAuction.startingBid,
                qAuction.currentBid,
                qAuction.startedAt,
                qAuction.endedAt,
                qAuction.status,
                qAuction.createdAt,
                qAuction.updatedAt);
    }
}
