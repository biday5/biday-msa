// Created on iPad.

// package shop.biday.model.entity;
package shop.biday.orderTest;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.ShipperEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

// 프론트에서 만든 orderId
    @Column(name="order_id", nullable=false)
    private String orderId;

// user
    @Column(name="user_id", nullable=false)
    private String userId;
    
    @Column(name="address", nullable=false)
    private String address;


// auction
    @Column(name="auction_id", nullable=false)
    private Long auctionId;


// 낙찰 정보
    @Column(name="award_id", nullable=false)
    private Long awardId;

    @Column(name="awarded_At", nullable=false)
    private LocalDateTime awardedAt;

    @Column(name="award_bid", nullable=false)
    private BigInteger awardBid;


// 상품 정보
    @Column(name="product_id", nullable=false)
    private Long productId;
    @Column(name="product_name", nullable=false)
    private String productName;
    @Column(name="size", nullable=false)
    private String size;

// 결제 정보
     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "payment_id", nullable = true)
     private PaymentEntity payment;
//    @Column(name="payment_id", nullable=false)
//    private Long payment;

// 배송지 정보
//     @OneToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "shipper_id", nullable = true)
//     private ShipperEntity shipper;
//    @Column(name="shipper_id", nullable=true)
//    private Long shipper;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}