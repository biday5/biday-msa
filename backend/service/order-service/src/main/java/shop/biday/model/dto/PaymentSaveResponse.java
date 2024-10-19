package shop.biday.model.dto;

import shop.biday.model.domain.PaymentCardModel;
import shop.biday.model.domain.PaymentEasyPay;

import java.time.LocalDateTime;

public record PaymentSaveResponse(
        Long id,
        String userId,
        Long amount,
        String orderId,
        String status,
        PaymentCardModel card,
        PaymentEasyPay easyPay,
        LocalDateTime approvedAt) {
}
