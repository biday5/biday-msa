package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
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
    private Long auctionId;
    private Long awardId;
    private BigInteger awardBid;
    private LocalDateTime awardedAt;
    private Long productId;
    private String productName;
    private String productSize;
    private PaymentDto payment;
    private String shipperName;
    private String streetAddress;
    private String detailAddress;
    private String contactNumber;
    private String contactEmail;
    private String sellerId;
    private String buyerId;
    private ShipperModel shipper;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
