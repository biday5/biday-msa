package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "shippers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ShipperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(name = "carrier", nullable = false)
    private String carrier;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "shipment_date", nullable = false)
    private LocalDateTime shipmentDate;

    @Column(name = "estimated_delivery_date", nullable = false)
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
