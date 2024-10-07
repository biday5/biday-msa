package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {

    private Long auctionId;

    private BigInteger currentBid;

    private boolean award;

    private Long count;

    private LocalDateTime bidedAt;
}
