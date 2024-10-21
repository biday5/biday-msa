package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.ShipperEntity;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ShipperModel {

    private Long id;
    private Long orderId;
    private String carrier;
    private String trackingNumber;
    private LocalDateTime shipmentDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime deliveryDate;
    private String status;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ShipperModel of(final ShipperEntity shipper) {
        return ShipperModel.builder()
                .orderId(shipper.getOrder().getId())
                .carrier(shipper.getCarrier())
                .trackingNumber(shipper.getTrackingNumber())
                .shipmentDate(shipper.getShipmentDate())
                .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                .deliveryDate(shipper.getDeliveryDate())
                .status(shipper.getStatus())
                .deliveryAddress(shipper.getDeliveryAddress())
                .createdAt(shipper.getCreatedAt())
                .updatedAt(shipper.getUpdatedAt())
                .build();
    }
}
