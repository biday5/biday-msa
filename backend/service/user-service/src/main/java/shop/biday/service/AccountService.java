package shop.biday.service;

import reactor.core.publisher.Mono;
import shop.biday.model.document.AccountDocument;
import shop.biday.model.domain.AccountModel;


public interface AccountService {
    Mono<AccountDocument> findByUserId(String id);

    Mono<AccountDocument> save(AccountModel accountModel);
}
