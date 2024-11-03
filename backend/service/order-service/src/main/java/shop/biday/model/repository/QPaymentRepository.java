package shop.biday.model.repository;

import shop.biday.model.dto.PaymentData;

import java.util.List;

public interface QPaymentRepository {

    List<PaymentData> findByUser(String userId);
}
