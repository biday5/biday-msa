package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.entity.QAuctionEntity;
import shop.biday.model.entity.QAwardEntity;
import shop.biday.model.repository.QAuctionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QAuctionRepositoryImpl implements QAuctionRepository {
    private final JPAQueryFactory queryFactory;

    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QAwardEntity qAward = QAwardEntity.awardEntity;

    @Override
    public AuctionModel findByAuctionId(Long id) {
        AuctionModel auction = queryFactory
                .select(Projections.constructor(AuctionModel.class,
                        qAuction.id,
                        qAuction.userId,
                        qAuction.sizeId,
                        qAuction.description,
                        qAuction.startingBid,
                        qAuction.currentBid,
                        qAuction.startedAt,
                        qAuction.endedAt,
                        qAuction.status,
                        qAuction.createdAt,
                        qAuction.updatedAt,
                        Projections.constructor(AwardDto.class,
                                qAward.id,
                                qAward.auction.id.as("auction"),
                                qAward.userId,
                                qAward.bidedAt,
                                qAward.currentBid,
                                qAward.count)))
                .from(qAuction)
                .leftJoin(qAuction.award, qAward)
                .where(qAuction.id.eq(id))
                .fetchFirst();

        return auction;
    }

    @Override
    public Slice<AuctionDto> findByUser(String user, String period, Long cursor, Pageable pageable) {
        LocalDateTime startDate = switch (period) {
            case "3개월" -> LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
            case "6개월" -> LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
            case "12개월" -> LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
            case "전체보기" -> null;
            default -> throw new IllegalArgumentException("Invalid period specified");
        };

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qAuction.startedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qAuction.id.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<AuctionDto> auctions = queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .where(qAuction.userId.eq(user)
                        .and(datePredicate)
                        .and(cursorPredicate))
                .orderBy(qAuction.endedAt.desc())
                .fetch();

        return createSlice(auctions, pageable);
    }

    @Override
    public Slice<AuctionDto> findBySize(Long sizeId, String order, Long cursor, Pageable pageable) {
        BooleanExpression sizePredicate = sizeId != null ? qAuction.sizeId.eq(sizeId) : null;
        BooleanExpression cursorPredicate = cursor != null ? qAuction.id.lt(cursor) : null;

        OrderSpecifier<?> datePredicate = switch (order) {
            case "종료 임박 순" -> qAuction.endedAt.asc();
            case "시작 순" -> qAuction.startedAt.asc();
            default -> qAuction.startedAt.asc();
        };

        List<AuctionDto> auctions = queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .where(
                        sizePredicate,
                        cursorPredicate,
                        qAuction.status.eq(false),
                        qAuction.endedAt.goe(LocalDateTime.now())
                )
                .orderBy(datePredicate)
                .fetch();
        return createSlice(auctions, pageable);
    }

    @Override
    public List<AuctionDto> findAllBySize(Long sizeId, String order) {
        OrderSpecifier<?> datePredicate = switch (order) {
            case "종료 임박 순" -> qAuction.endedAt.asc();
            case "시작 순" -> qAuction.startedAt.asc();
            default -> qAuction.createdAt.desc();
        };

        return queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .where(
                        qAuction.sizeId.eq(sizeId),
                        qAuction.status.eq(false),
                        qAuction.endedAt.goe(LocalDateTime.now())
                )
                .orderBy(datePredicate)
                .fetch();
    }

    private ConstructorExpression<AuctionDto> createAuctionDtoProjection() {
        return Projections.constructor(AuctionDto.class,
                qAuction.id,
                qAuction.userId,
                qAuction.sizeId,
                qAuction.description,
                qAuction.startingBid,
                qAuction.currentBid,
                qAuction.startedAt,
                qAuction.endedAt,
                qAuction.status,
                qAuction.createdAt,
                qAuction.updatedAt);
    }

    private Slice<AuctionDto> createSlice(List<AuctionDto> auctions, Pageable pageable) {
        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }
        return new SliceImpl<>(auctions, pageable, hasNext);
    }
}
