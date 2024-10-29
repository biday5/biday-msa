package shop.biday.model.repository;

import shop.biday.model.dto.PaymentData;
import shop.biday.model.dto.PaymentRequest;

import java.util.List;

public interface QPaymentRepository {

    List<PaymentData> findByUser(String userId);
}
