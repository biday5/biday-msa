package shop.biday.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;
import shop.biday.model.domain.AddressModel;


public interface AddressService {
    Flux<AddressDocument> findAllByUserId(String userInfoHeader);
    Mono<String> pick(String id);
    Mono<Long> countByUserId(String userInfoHeader);
    Mono<AddressDocument> save(String userInfoHeader , AddressModel addressModel);
    Mono<Boolean> deleteById(String userInfoHeader, String id);

//    Flux<AddressDocument> findAll();
//    Mono<AddressDocument> findById(String id);
//    Mono<Boolean> existsById(String id);
//    Mono<Long> count();
}
