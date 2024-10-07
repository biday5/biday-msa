package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.util.List;

public interface AuctionService {
    AuctionModel findById(Long id);

    Mono<AuctionDto> findByAuctionId(Long auctionId);

    Slice<AuctionDto> findBySize(Long sizeId, String order, Long cursor, Pageable pageable);

    List<AuctionDto> findAllBySize(Long sizeId, String order);

    Slice<AuctionDto> findByUser(String userInfoHeader, String period, Long cursor, Pageable pageable);

    AuctionEntity updateState(Long id);

    boolean existsById(Long id);

    AuctionEntity save(String userInfoHeader, AuctionModel auction);

    AuctionEntity update(String userInfoHeader, AuctionModel auction);

    String deleteById(String userInfoHeader, Long id);
}
