package shop.biday.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import shop.biday.model.document.LoginHistoryDocument;

import java.time.LocalDateTime;

@Repository
public interface MLoginHistoryRepository extends ReactiveMongoRepository<LoginHistoryDocument, String> {
    Mono<LoginHistoryDocument> findFirstByUserIdAndCreatedAtBetween(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
