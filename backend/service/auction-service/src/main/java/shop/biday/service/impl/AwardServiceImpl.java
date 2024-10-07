package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.AwardEntity;
import shop.biday.model.repository.AwardRepository;
import shop.biday.service.AwardService;
import shop.biday.utils.UserInfoUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public List<AwardEntity> findAll() {
        log.info("Find all awards");
        return awardRepository.findAll();
    }

    @Override
    public AwardEntity findById(Long id) {
        log.info("Find award by id: {}", id);
        return awardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Award not found for id: {}", id);
                    return new IllegalArgumentException("유효하지 않은 데이터입니다.");
                });
    }

    @Override
    public AwardEntity save(AwardEntity award) {
        log.info("Saving award: {}", award);
        AwardEntity savedAward = awardRepository.save(award);
        log.info("Successfully saved award with id: {}", savedAward.getId());
        return savedAward;
    }

    @Override
    public AwardModel findByAwardId(String userInfoHeader, Long awardId) {
        log.info("Find User {} Award by Id: {}", userInfoHeader, awardId);
        return validateUser(userInfoHeader)
                .flatMap(uid -> awardRepository.findById(awardId)
                        .filter(award -> {
                            boolean isAuthorized = award.getUserId().equals(uid);
                            if (isAuthorized) {
                                log.info("User {} is authorized for award id {}", uid, awardId);
                            } else {
                                log.warn("User {} is not authorized for award id {}", uid, awardId);
                            }
                            return isAuthorized;
                        })
                        .map(award -> {
                            log.info("Award found for User {}: {}", uid, awardId);
                            return awardRepository.findByAwardId(awardId);
                        }))
                .orElseGet(() -> {
                    log.error("User {} not found or not authorized for award id {}", userInfoUtils.extractUserInfo(userInfoHeader).getUserId(), awardId);
                    return null;
                });
    }

    @Override
    public Slice<AwardModel> findByUser(String userInfoHeader, String period, LocalDateTime cursor, Pageable pageable) {
        log.info("Finding awards for User: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(uid -> {
                    log.info("Valid user {} found, fetching awards.", uid);
                    return awardRepository.findByUser(uid, period, cursor, pageable);
                })
                .orElseGet(() -> {
                    log.error("Invalid user ID: {}", userInfoUtils.extractUserInfo(userInfoHeader).getUserId());
                    return null;
                });
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserId())
                .filter(uid -> {
                    boolean isValid = !uid.isEmpty();
                    if (!isValid) {
                        log.error("Invalid user ID: {}", uid);
                    }
                    return isValid;
                });
    }
}