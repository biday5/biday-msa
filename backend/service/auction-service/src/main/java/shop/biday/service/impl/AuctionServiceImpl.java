package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.repository.AuctionRepository;
import shop.biday.scheduler.QuartzService;
import shop.biday.service.AuctionService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final QuartzService quartzService;
    private final UserInfoUtils userInfoUtils;

    @Override
    public ResponseEntity<AuctionModel> findById(Long id) {
        log.info("Find Auction by id: {}", id);
        return Optional.ofNullable(auctionRepository.findByAuctionId(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Auction not found for id: {}", id);
                    return ResponseEntity.status(404).body(null);
                });
    }

    @Override
    public Mono<AuctionDto> findByAuctionId(Long auctionId) {
        return Mono.fromCallable(() -> auctionRepository.findById(auctionId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(auctionEntity -> auctionEntity
                        .map(auction -> AuctionDto.builder()
                                .id(auction.getId())
                                .currentBid(auction.getCurrentBid())
                                .startedAt(auction.getStartedAt())
                                .build())
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Exists Auction by id: {}", id);
        return auctionRepository.existsById(id);
    }

    @Override
    public ResponseEntity<Slice<AuctionDto>> findBySize(Long sizeId, String order, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By Time: {} SizeId: {}", order, sizeId);
        return ResponseEntity.ok(auctionRepository.findBySize(sizeId, order, cursor, pageable));
    }

    @Override
    public ResponseEntity<List<AuctionDto>> findAllBySize(Long sizeId, String order) {
        log.info("Find All by Size: {} Order: {}", sizeId, order);
        return ResponseEntity.ok(auctionRepository.findAllBySize(sizeId, order));
    }

    @Override
    public ResponseEntity<Slice<AuctionDto>> findByUser(String userInfoHeader, String period, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By User: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(t -> ResponseEntity.ok(auctionRepository.findByUser(userInfoUtils.extractUserInfo(userInfoHeader).getUserId(), period, cursor, pageable)))
                .orElseGet(() -> ResponseEntity.status(403).body(null));
    }

    @Override
    public ResponseEntity<AuctionEntity> updateState(Long id) {
        log.info("Update Auction Status by id: {}", id);
        return auctionRepository.findById(id)
                .map(auction -> {
                    auction.setStatus(true);
                    auctionRepository.save(auction);
                    return ResponseEntity.ok(auction);
                })
                .orElseThrow(() -> new NoSuchElementException("Auction not found with id: " + id));
    }

    @Override
    public ResponseEntity<AuctionEntity> save(String userInfoHeader, AuctionDto auction) {
        log.info("Save Auction started");
        return validateUser(userInfoHeader)
                .map(t -> {
                    AuctionEntity auctionEntity = auctionRepository.save(AuctionEntity.builder()
                            .userId(userInfoUtils.extractUserInfo(userInfoHeader).getUserId())
                            .sizeId(auction.getSizeId())
                            .description(auction.getDescription())
                            .startingBid(auction.getStartingBid())
                            .currentBid(auction.getCurrentBid())
                            .startedAt(auction.getStartedAt())
                            .endedAt(auction.getEndedAt())
                            .status(false)
                            .build());

                    quartzService.createJob(auctionEntity.getId(), auctionEntity.getEndedAt());
                    return ResponseEntity.ok(auctionEntity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid role or not a seller"));
    }

    @Override
    public ResponseEntity<AuctionEntity> update(String userInfoHeader, AuctionDto auction) {
        log.info("Update Auction started for id: {}", auction.getId());
        return validateUser(userInfoHeader)
                .map(t -> {
                    Long auctionId = auction.getId();
                    String auctionUserId = auctionRepository.findById(auctionId)
                            .orElseThrow(() -> new IllegalArgumentException("Auction not found"))
                            .getUserId();

                    if (!auctionUserId.equals(userInfoUtils.extractUserInfo(userInfoHeader).getUserId())) {
                        log.error("User with ID {} does not have Update Authority for auction id: {}", userInfoUtils.extractUserInfo(userInfoHeader).getUserId(), auctionId);
                        throw new SecurityException("User does not have permission to update this auction.");
                    }

                    AuctionEntity auctionEntity = auctionRepository.save(AuctionEntity.builder()
                            .userId(auction.getUserId())
                            .sizeId(auction.getSizeId())
                            .description(auction.getDescription())
                            .startingBid(auction.getStartingBid())
                            .currentBid(auction.getCurrentBid())
                            .startedAt(auction.getStartedAt())
                            .endedAt(auction.getEndedAt())
                            .status(false)
                            .build());
                    log.debug("Update Auction By User for id: {}", auctionId);
                    return ResponseEntity.ok(auctionEntity);
                })
                .orElseThrow(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return new IllegalArgumentException("Invalid role or user does not exist.");
                });
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfoHeader, Long id) {
        log.info("Delete Auction started for id: {}", id);
        return validateUser(userInfoHeader)
                .map(t -> {
                    AuctionEntity auction = auctionRepository.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("Auction not found"));

                    if (!auction.getUserId().equals(userInfoUtils.extractUserInfo(userInfoHeader).getUserId())) {
                        log.error("User with ID {} does not have Delete Authority for auction id: {}", userInfoUtils.extractUserInfo(userInfoHeader).getUserId(), id);
                        return ResponseEntity.status(403).body("삭제 권한이 없습니다");
                    }

                    auctionRepository.deleteById(id);
                    log.debug("Delete Auction By User for id: {}", id);
                    return ResponseEntity.ok("경매 삭제 성공");
                })
                .orElseGet(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return ResponseEntity.status(403).body("유효하지 않은 사용자");
                });
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserRole())
                .filter(t -> t.equalsIgnoreCase("ROLE_SELLER"))
                .or(() -> {
                    log.error("User does not have role SELLER: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }
}
