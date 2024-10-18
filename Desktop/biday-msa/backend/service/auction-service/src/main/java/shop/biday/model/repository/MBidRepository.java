package shop.biday.model.repository;

import reactor.core.publisher.Mono;
import shop.biday.model.document.BidDocument;

public interface MBidRepository {

    Mono<BidDocument> findFirstByAuctionIdSorted(Long auctionId);

    Mono<Long> countByAuctionId(Long auctionId);

    Mono<Boolean> updateAward(String id);

    Mono<Long> countByAuctionIdAndUserId(Long auctionId, String userId);
}
