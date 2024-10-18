package shop.biday.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import shop.biday.model.document.UserDocument;


@Repository
public interface MUserRepository extends ReactiveMongoRepository<UserDocument,  String> {
    Mono<UserDocument> findById(String id);

    Mono<UserDocument> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByPhone(String phone);

    Mono<String> findEmailByPhone(String phone);

    Mono<Boolean> existsById(String id);

    Mono<Void> deleteById(String id);

}
