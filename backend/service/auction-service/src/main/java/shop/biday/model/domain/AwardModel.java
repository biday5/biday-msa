package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AuctionDto;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AwardModel {
    private Long id;
    private AuctionDto auction;
    private String userId;
    private LocalDateTime bidedAt;
    private BigInteger currentBid;
    private Long count;
}
