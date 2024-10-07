package shop.biday.service;

import shop.biday.model.domain.PaymentTempModel;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.dto.PaymentResponse;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.PaymentStatus;

import java.util.List;

public interface PaymentService {

    void savePaymentTemp(String userInfo, PaymentTempModel paymentTempModel);

    Boolean save(String userInfo, PaymentRequest paymentRequest);

    PaymentEntity findById(Long id);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    void delete(PaymentEntity paymentEntity);

    PaymentResponse findPaymentByPaymentKey(Long id);

    PaymentEntity updateCancelStatus(Long id, PaymentStatus paymentStatus);

    List<PaymentRequest> findByUser(String user);
}
