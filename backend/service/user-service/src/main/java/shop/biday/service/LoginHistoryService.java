package shop.biday.service;

import reactor.core.publisher.Mono;

public interface LoginHistoryService {
    Mono<Boolean> findByUserId(String userId);
}
