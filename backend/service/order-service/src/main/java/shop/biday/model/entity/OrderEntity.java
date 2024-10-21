package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

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

    // auction
    @Column(name="auction_id", nullable=false)
    private Long auctionId;

    // 낙찰 정보
    @Column(name="award_id", nullable=false)
    private Long awardId;
    @Column(name="award_bid", nullable=false)
    private BigInteger awardBid;
    @Column(name="awarded_at", nullable=false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime awardedAt;

// 상품 정보
    @Column(name="product_id", nullable=false)
    private Long productId;
    @Column(name="product_name", nullable=false)
    private String productName;
    @Column(name="product_size", nullable=false)
    private String productSize;

// 결제 정보
     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "payment_id", nullable = false)
     private PaymentEntity payment;

// 주문 정보
    @Column(name="shipper_name", nullable=false)
    private String shipperName;
    @Column(name="recipient_name", nullable=false)
    private String recipientName;
    @Column(name="street_address", nullable=false)
    private String streetAddress;
    @Column(name="detail_address", nullable=false)
    private String detailAddress;
    @Column(name="contact_number", nullable=false)
    private String contactNumber;
    @Column(name="contact_email", nullable=false)
    private String contactEmail;

    // 접근가능한 userID
    @Column(name="seller_id", nullable=false)
    private String seller;
    @Column(name="buyer_id", nullable=false)
    private String buyer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // 배송 정보
    @OneToOne(mappedBy = "order")
    private ShipperEntity shipper;
}