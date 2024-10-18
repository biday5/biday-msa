package shop.biday.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;

import shop.biday.model.domain.AddressRequest;


public interface AddressService {
    Flux<AddressDocument> findAllByUserId(String userInfoHeader);
    Mono<String> pick(String id);
    Mono<Long> countByUserId(String userInfoHeader);
    Mono<AddressDocument> save(String userInfoHeader , AddressRequest addressRequest);
    Mono<Boolean> deleteById(String userInfoHeader, String id);

}
