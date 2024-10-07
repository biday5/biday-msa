package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AwardDto;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuctionModel {
    private Long id;
    private String user;
    private Long size;
    private String description;
    private Long startingBid;
    private Long currentBid;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AwardDto award;
}
