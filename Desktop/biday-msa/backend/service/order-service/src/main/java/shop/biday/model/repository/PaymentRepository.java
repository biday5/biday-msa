package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>, QPaymentRepository {

    Optional<PaymentEntity> findById(Long id);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    void delete(PaymentEntity paymentEntity);
}
