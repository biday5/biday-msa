package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.FaqEntity;

public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
}
