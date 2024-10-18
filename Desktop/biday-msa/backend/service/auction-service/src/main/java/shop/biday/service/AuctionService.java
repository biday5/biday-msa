package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.util.List;

public interface AuctionService {
    ResponseEntity<AuctionModel> findById(Long id);

    Mono<AuctionDto> findByAuctionId(Long auctionId);

    ResponseEntity<Slice<AuctionDto>> findBySize(Long sizeId, String order, Long cursor, Pageable pageable);

    ResponseEntity<List<AuctionDto>> findAllBySize(Long sizeId, String order);

    ResponseEntity<Slice<AuctionDto>> findByUser(String userInfoHeader, String period, Long cursor, Pageable pageable);

    AuctionEntity updateState(Long id);

    boolean existsById(Long id);

    ResponseEntity<AuctionEntity> save(String userInfoHeader, AuctionDto auction);

    ResponseEntity<AuctionEntity> update(String userInfoHeader, AuctionDto auction);

    ResponseEntity<String> deleteById(String userInfoHeader, Long id);
}
