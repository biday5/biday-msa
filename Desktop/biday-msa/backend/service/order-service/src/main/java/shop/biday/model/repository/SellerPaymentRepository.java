package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.SellerPaymentEntity;

public interface SellerPaymentRepository extends JpaRepository<SellerPaymentEntity, Long> {
}
