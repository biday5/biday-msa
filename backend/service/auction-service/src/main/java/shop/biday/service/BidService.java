package shop.biday.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.BidDocument;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidDto;
import shop.biday.model.dto.BidResponse;

public interface BidService {

    Flux<BidDto> findByUserId(String userInfo);

    Mono<BidResponse> save(String userInfo, BidModel bid);

    Mono<BidDocument> findTopBidByAuctionId(Long auctionId);

    Mono<Boolean> updateAward(Long auctionId);

    Mono<Long> countBidByAuctionIdAndUserId(Long auctionId, String userId);

    Mono<Long> countByAuctionId(Long auctionId);
}
