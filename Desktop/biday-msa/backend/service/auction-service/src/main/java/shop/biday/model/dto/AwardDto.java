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
public class AwardDto {
    private Long id;
    private Long auction;
    private String userId;
    private LocalDateTime bidedAt;
    private BigInteger currentBid;
    private Long count;
}
