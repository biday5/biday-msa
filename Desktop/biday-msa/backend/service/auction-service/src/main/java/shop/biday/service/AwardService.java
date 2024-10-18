package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.entity.AwardEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AwardService {

    List<AwardEntity> findAll();

    AwardEntity findById(Long id);

    AwardEntity save(AwardEntity award);

    ResponseEntity<AwardModel> findByAwardId(String userInfoHeader, Long awardId);

    ResponseEntity<Slice<AwardModel>> findByUser(String userInfoHeader, String period, LocalDateTime cursor, Pageable pageable);
}
