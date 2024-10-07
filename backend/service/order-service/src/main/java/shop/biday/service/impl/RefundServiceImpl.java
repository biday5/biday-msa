package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.PaymentCancelModel;
import shop.biday.model.domain.PaymentModel;
import shop.biday.model.dto.RefundRequest;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.PaymentStatus;
import shop.biday.model.entity.RefundEntity;
import shop.biday.model.repository.RefundRepository;
import shop.biday.service.PaymentService;
import shop.biday.service.RefundService;
import shop.biday.utils.TossPaymentTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final PaymentService paymentService;
    private final RefundRepository refundRepository;
    private final TossPaymentTemplate tossPaymentTemplate;

    @Override
    public boolean cancel(Long id, RefundRequest cancelRequest) {
        PaymentEntity payment = paymentService.findById(id);
        String cancelUri = String.format("%s/cancel", payment.getPaymentKey());

        ResponseEntity<PaymentModel> response = tossPaymentTemplate.exchangePostMethod(cancelUri, cancelRequest);
        PaymentModel paymentModel = tossPaymentTemplate.getPayment(response);
        PaymentCancelModel cancel = getCancel(paymentModel.getCancels());

        ZonedDateTime canceledAt = ZonedDateTime.parse(cancel.getCanceledAt(), DATE_TIME_FORMATTER);
        RefundEntity refundEntity = RefundEntity.builder()
                .payment(payment)
                .transactionKey(cancel.getTransactionKey())
                .reason(cancel.getCancelReason())
                .canceledAt(canceledAt.toLocalDateTime())
                .amount(cancel.getCancelAmount())
                .status(cancel.getCancelStatus())
                .build();

        refundRepository.save(refundEntity);
        paymentService.updateCancelStatus(id, PaymentStatus.fromStatus(paymentModel.getStatus()));
        return true;
    }

    private PaymentCancelModel getCancel(List<PaymentCancelModel> cancels) {
        return PaymentCancelModel.builder()
                .transactionKey(cancels.get(0).getTransactionKey())
                .cancelReason(cancels.get(0).getCancelReason())
                .cancelAmount(cancels.get(0).getCancelAmount())
                .canceledAt(cancels.get(0).getCanceledAt())
                .cancelStatus(cancels.get(0).getCancelStatus())
                .build();
    }
}
