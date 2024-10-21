// package shop.biday.model.repository.impl;
package shop.biday.orderTest;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
// import shop.biday.model.domain.AwardModel;
// import shop.biday.model.dto.AuctionDto;
// import shop.biday.model.entity.QAuctionEntity;
// import shop.biday.model.entity.QAwardEntity;
// import shop.biday.model.repository.QAwardRepository;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.dto.PaymentDto;
import shop.biday.orderTest.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QOrderRepositoryImpl implements QOrderRepository {
    private final JPAQueryFactory queryFactory;

    private final QOrderEntity qOrder = QOrderEntity.orderEntity;
    private final QPaymentEntity qPayment = QPaymentEntity.paymentEntity;
    private final QShipperEntity qShipper = QShipperEntity.ShipperEntity;

    @Override
    public OrderModel findByOrderId(Long id) {
        return queryFactory
                .select(createOrderModelProjection())
                .from(qOrder)
//                .leftJoin(qOrder.payment, qPayment)
//                .leftJoin(qOrder.shipper, qShipper)
                .where(qOrder.id.eq(id)
                        .and(qPayment.id.eq(qOrder.paymentId))
                        .and(qShipper.id.eq(qOrder.shipperId))
                )
                .fetchOne();
    }

    @Override
    public Slice<OrderModel> findByUser(String userId, String period, LocalDateTime cursor, Pageable pageable) {
        LocalDateTime startDate = switch (period) {
            case "3개월" -> LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
            case "6개월" -> LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
            case "12개월" -> LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
            case "전체보기" -> null;
            default -> throw new IllegalArgumentException("Invalid period specified");
        };

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qOrder.awardedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qOrder.awardedAt.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<OrderModel> orders = queryFactory
                .select(createOrderModelProjection())
                .from(qOrder)
//                .leftJoin(qOrder.payment, qPayment)
//                .leftJoin(qOrder.shipper, qShipper)
                .where(
                        qOrder.userId.contains(userId)
                        .and(datePredicate)
                        .and(cursorPredicate)
                                .and(qPayment.id.eq(qOrder.paymentId))
                                .and(qShipper.id.eq(qOrder.shipperId))
                )
                .orderBy(qOrder.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = orders.size() > pageable.getPageSize();
        if (hasNext) {
            orders.remove(orders.size() - 1);
        }

        return new SliceImpl<>(orders, pageable, hasNext);
    }

// todo 수정하기
    private ConstructorExpression<OrderModel> createOrderModelProjection() {
        return Projections.constructor(OrderModel.class,
                qOrder.id,
                qOrder.orderId,
                qOrder.userId,
                qOrder.address,
                qOrder.actionId,
                qOrder.awardId,
                qOrder.awardedAt,
                qOrder.awardBid,
                qOrder.productId,
                qOrder.productName,
                qOrder.size,
                createPaymentDtoProjection(),
                createShipperModelProjection(),
                qOrder.createdAt,
                qOrder.updatedAt)
                ;
    }
// payment, shipper 수정
    private ConstructorExpression<PaymentDto> createPaymentDtoProjection() {
        return Projections.constructor(PaymentDto.class,
                qPayment.orderId,
                qPayemnt.userId,
                qPayment.awardId,
                qPayment.amount);
    }

    private ConstructorExpression<ShipperModel> createShipperModelProjection() {
        return Projections.constructor(ShipperModel.class,
                qShipper.id,
                qShipper.paymentId,
                qShipper.carrier,
                qShipper.trackingNumber,
                qShipper.shipmentDate,
                qShipper.estimatedDeliveryDate,
                qShipper.deliveryDate,
                qShipper.status,
                qShipper.deliveryAddress,
                qShipper.createdAt,
                qShipper.updatedAt);
    }
}
