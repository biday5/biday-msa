package shop.biday.model.repository;

import shop.biday.model.dto.PaymentRequest;

import java.util.List;

public interface QPaymentRepository {

    List<PaymentRequest> findByUser(String userId);
}
