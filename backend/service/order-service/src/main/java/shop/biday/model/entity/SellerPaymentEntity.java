package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table(name = "seller_payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "fee", nullable = false)
    private Long fee;

    @Column(name = "net_amount", nullable = false)
    private Long netAmount;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'BANK_TRANSFER'")
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
