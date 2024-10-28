package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.biday.model.document.BidDocument;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidDto {

    private String id;

    private Long auctionId;

    private String userId;

    private BigInteger currentBid;

    private boolean award;

    private LocalDateTime bidedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static BidDto convertToDto(BidDocument bidDocument) {
        return BidDto.builder()
                .id(bidDocument.getId())
                .auctionId(bidDocument.getAuctionId())
                .userId(bidDocument.getUserId())
                .currentBid(bidDocument.getCurrentBid())
                .award(bidDocument.isAward())
                .bidedAt(bidDocument.getBidedAt())
                .createdAt(bidDocument.getCreatedAt())
                .updatedAt(bidDocument.getUpdatedAt())
                .build();
    }
}

