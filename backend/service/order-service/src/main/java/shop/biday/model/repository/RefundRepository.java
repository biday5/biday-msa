package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.RefundEntity;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
}
