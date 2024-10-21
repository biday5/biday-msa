// package shop.biday.model;
package shop.biday.orderTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import shop.biday.model.domain.ShipperModel;
import shop.biday.model.dto.PaymentDto;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private Long id;
    private String orderId;
    private String userId;
    private String address;
    private Long auctionId;
    private Long awardId;
    private LocalDateTime awardedAt;
    private BigInteger awardBid;
    private Long productId;
    private String productName;
    private String size;
    private PaymentDto payment;
    private ShipperModel shipper;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
