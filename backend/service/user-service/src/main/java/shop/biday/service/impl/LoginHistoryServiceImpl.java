package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.biday.model.document.LoginHistoryDocument;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.model.repository.MLoginHistoryRepository;
import shop.biday.service.LoginHistoryService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final MLoginHistoryRepository loginHistoryRepository;

    public Mono<LoginHistoryDocument> save(LoginHistoryModel loginHistoryModel) {
        String userId = loginHistoryModel.getUserId();
        if (userId == null) {
            return Mono.error(new IllegalArgumentException("요청한 유저 아이디 값이 null값 입니다."));
        }

        LoginHistoryDocument loginHistoryEntity = LoginHistoryDocument.builder()
                .userId(userId)
                .build();

        return loginHistoryRepository.save(loginHistoryEntity);
    }

    @Override
    public Mono<Boolean> findByUserId(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        return loginHistoryRepository.findFirstByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay)
                .map(history -> true)
                .defaultIfEmpty(false);
    }
}
