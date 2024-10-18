package shop.biday.model.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import shop.biday.model.document.BidDocument;

@Repository
public interface BidRepository extends ReactiveMongoRepository<BidDocument, String>, MBidRepository {

    @Query(value = "{ 'auctionId': ?0 }", sort = "{ 'currentBid': -1, 'bidedAt': 1 }")
    Flux<BidDocument> findFirstByAuctionIdOrderByCurrentBidDescAndBidedAtAsc(Long auctionId);

}