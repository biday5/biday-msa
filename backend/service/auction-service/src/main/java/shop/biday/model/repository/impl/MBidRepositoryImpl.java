package shop.biday.model.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import shop.biday.model.document.BidDocument;
import shop.biday.model.repository.MBidRepository;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MBidRepositoryImpl implements MBidRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<BidDocument> findFirstByAuctionIdSorted(Long auctionId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(where("auctionId").is(auctionId)),
                Aggregation.sort(Sort.by(desc("currentBid"), asc("bidedAt"))),
                Aggregation.limit(1)
        );
        return mongoTemplate.aggregate(aggregation, BidDocument.class, BidDocument.class).next();
    }

    @Override
    public Mono<Long> countByAuctionId(Long auctionId) {
        Query query = new Query();
        query.addCriteria(where("auctionId")
                .is(auctionId));
        return mongoTemplate.count(query, BidDocument.class);
    }

    @Override
    public Mono<Boolean> updateAward(String id) {
        return mongoTemplate.updateFirst(
                        query(where("id").is(id)),
                        update("award", true),
                        BidDocument.class
                )
                .map(result -> result.getModifiedCount() > 0);
    }

    @Override
    public Mono<Long> countByAuctionIdAndUserId(Long auctionId, String userId) {
        Query query = new Query();
        query.addCriteria(where("auctionId").is(auctionId)
                .and("userId").is(userId));
        return mongoTemplate.count(query, BidDocument.class);
    }
}
