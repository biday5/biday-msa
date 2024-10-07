package shop.biday.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;
import shop.biday.model.domain.AddressModel;


public interface AddressService {
    Flux<AddressDocument> findAll();
    Mono<AddressDocument> findById(String id);
    Mono<AddressDocument> save(String token , AddressModel addressModel);
    Mono<Boolean> existsById(String id);
    Mono<Long> count();
    Mono<Boolean> deleteById(String id);
}
