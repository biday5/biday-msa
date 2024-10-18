package shop.biday.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AccountDocument;


@Repository
public interface MAccountRepository extends ReactiveMongoRepository<AccountDocument, String> {
    Mono<AccountDocument> findByUserId(String userId);
}
