package shop.biday.model.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;

@Repository
public interface MAddressRepository extends ReactiveMongoRepository<AddressDocument, String> {
    Mono<Long> countByUserId(String id);

    Mono<AddressDocument> findByUserIdAndPick(String userId, Boolean pick);

    Mono<AddressDocument> findByUserId(String id);

    Flux<AddressDocument> findAllByUserId(String id);
}