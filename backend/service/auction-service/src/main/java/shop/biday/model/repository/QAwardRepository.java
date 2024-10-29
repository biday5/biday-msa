package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AwardModel;

import java.time.LocalDateTime;

public interface QAwardRepository {
    AwardModel findByAwardId(Long id);

    Slice<AwardModel> findByUser(String user, String period, LocalDateTime cursor, Pageable pageable);
}
