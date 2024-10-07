package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.biday.model.document.BidDocument;
import shop.biday.model.domain.BidModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.dto.BidResponse;
import shop.biday.model.repository.BidRepository;
import shop.biday.service.BidService;
import shop.biday.utils.UserInfoUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Mono<BidResponse> save(String userInfo, BidModel bid) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);

        BidDocument bidDocument = BidDocument.builder()
                .auctionId(bid.auctionId())
                .userId(userInfoModel.getUserId())
                .currentBid(bid.currentBid())
                .build();

        return bidRepository.save(bidDocument)
                .flatMap(savedBid -> bidRepository.findFirstByAuctionIdSorted(bid.auctionId()))
                .log()
                .flatMap(findBid -> bidRepository.countByAuctionId(findBid.getAuctionId())
                        .log()
                        .map(count -> BidResponse.builder()
                                .auctionId(findBid.getAuctionId())
                                .currentBid(findBid.getCurrentBid())
                                .award(findBid.isAward())
                                .bidedAt(findBid.getBidedAt())
                                .count(count)
                                .build()
                        )
                );
    }

    @Override
    public Mono<BidDocument> findTopBidByAuctionId(Long auctionId) {
        return bidRepository.findFirstByAuctionIdOrderByCurrentBidDescAndBidedAtAsc(auctionId)
                .next()
                .log();
    }

    @Override
    public Mono<Boolean> updateAward(Long auctionId) {
        return bidRepository.findFirstByAuctionIdOrderByCurrentBidDescAndBidedAtAsc(auctionId)
                .next()
                .flatMap(findBid -> bidRepository.updateAward(findBid.getId()))
                .log();
    }

    @Override
    public Mono<Long> countBidByAuctionIdAndUserId(Long auctionId, String userId) {
        return bidRepository.countByAuctionIdAndUserId(auctionId, userId);
    }

    @Override
    public Mono<Long> countByAuctionId(Long auctionId) {
        return bidRepository.countByAuctionId(auctionId);
    }
}
