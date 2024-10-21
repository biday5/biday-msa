// package shop.biday.model.repository;
package shop.biday.orderTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
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
    private Long paymentId;
//    private Long shipperId;
}