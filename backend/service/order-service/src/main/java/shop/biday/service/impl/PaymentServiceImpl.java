package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.exception.PaymentException;
import shop.biday.model.domain.PaymentCardModel;
import shop.biday.model.domain.PaymentModel;
import shop.biday.model.domain.PaymentTempModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.dto.PaymentDto;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.dto.PaymentResponse;
import shop.biday.model.entity.PaymentCardType;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.PaymentMethod;
import shop.biday.model.entity.PaymentStatus;
import shop.biday.model.repository.PaymentRepository;
import shop.biday.service.PaymentService;
import shop.biday.utils.RedisTemplateUtils;
import shop.biday.utils.TossPaymentTemplate;
import shop.biday.utils.UserInfoUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final String APPROVE_URI = "confirm";

    private final PaymentRepository paymentRepository;
    private final TossPaymentTemplate tossPaymentTemplate;
    private final RedisTemplateUtils<PaymentDto> redisTemplateUtils;
    private final UserInfoUtils userInfoUtils;

    @Override
    public PaymentEntity findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentException(
                        HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다.")
                );
    }

    @Override
    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public long count() {
        return paymentRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public void delete(PaymentEntity paymentEntity) {
        paymentRepository.delete(paymentEntity);
    }

    @Override
    public void savePaymentTemp(String userInfo, PaymentTempModel paymentTempModel) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);
        redisTemplateUtils.save(paymentTempModel.orderId(), PaymentDto.builder()
                .orderId(paymentTempModel.orderId())
                .userId(userInfoModel.getUserId())
                .awardId(paymentTempModel.awardId())
                .amount(paymentTempModel.amount())
                .build());
    }

    @Override
    public Boolean save(String userInfo, PaymentRequest paymentRequest) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);

        PaymentDto paymentDto = getPaymentTempModel(paymentRequest);
        if (!isCheckPaymentData(paymentRequest, userInfoModel.getUserId(), paymentDto)) {
            throw new PaymentException(HttpStatus.BAD_REQUEST, "INVALID_DATA_REQUEST", "일치하지 않는 정보 입니다.");
        }

        ResponseEntity<PaymentModel> response = tossPaymentTemplate.exchangePostMethod(APPROVE_URI, paymentRequest);
        PaymentModel paymentModel = tossPaymentTemplate.getPayment(response);

        ZonedDateTime requestedAt = ZonedDateTime.parse(paymentModel.getRequestedAt(), DATE_TIME_FORMATTER);
        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);

        PaymentEntity payment = PaymentEntity.builder()
                .userId(userInfoModel.getUserId())
                .awardId(paymentRequest.awardId())
                .paymentKey(paymentModel.getPaymentKey())
                .type(paymentModel.getType())
                .orderId(paymentRequest.orderId())
                .currency(paymentModel.getCurrency())
                .paymentMethod(PaymentMethod.fromName(paymentModel.getMethod()))
                .totalAmount(paymentModel.getTotalAmount())
                .balanceAmount(paymentModel.getBalanceAmount())
                .paymentStatus(PaymentStatus.fromStatus(paymentModel.getStatus()))
                .requestedAt(requestedAt.toLocalDateTime())
                .approvedAt(approvedAt.toLocalDateTime())
                .suppliedAmount(paymentModel.getSuppliedAmount())
                .vat(paymentModel.getVat())
                .build();

        paymentRepository.save(payment);
        deletePaymentTempModel(paymentRequest.orderId());
        return true;
    }

    @Override
    public PaymentResponse findPaymentByPaymentKey(Long id) {
        PaymentEntity payment = findById(id);
        log.info("payment: {}", payment);

        ResponseEntity<PaymentModel> response = tossPaymentTemplate.exchangeGetMethod(payment.getPaymentKey());
        PaymentModel paymentModel = tossPaymentTemplate.getPayment(response);

        PaymentCardModel card = paymentModel.getCard();
        card.setIssuerName(PaymentCardType.getByCode(card.getIssuerCode()).getName());

        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);

        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getAwardId(),
                paymentModel.getTotalAmount(),
                paymentModel.getMethod(),
                paymentModel.getOrderId(),
                paymentModel.getStatus(),
                card,
                paymentModel.getEasyPay(),
                approvedAt.toLocalDateTime()
        );
    }

    @Override
    public PaymentEntity updateCancelStatus(Long id, PaymentStatus paymentStatus) {
        PaymentEntity payment = findById(id);
        payment.setPaymentStatus(paymentStatus);
        return paymentRepository.save(payment);
    }

    @Override
    public List<PaymentRequest> findByUser(String userInfo) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);
        log.info("Find Payment By UserInfo: {}", userInfo);
        return paymentRepository.findByUser(userInfoModel.getUserId());
    }

    private boolean isCheckPaymentData(PaymentRequest paymentRequest, String userId, PaymentDto paymentDto) {
        if (paymentDto == null) {
            return false;
        }
        return Objects.equals(paymentDto.getOrderId(), paymentRequest.orderId()) &&
                Objects.equals(paymentDto.getUserId(), userId) &&
                Objects.equals(paymentDto.getAwardId(), paymentRequest.awardId()) &&
                Objects.equals(paymentDto.getAmount(), paymentRequest.amount());
    }

    private void deletePaymentTempModel(String key) {
        redisTemplateUtils.delete(key);
    }

    private PaymentDto getPaymentTempModel(PaymentRequest paymentRequest) {
        return redisTemplateUtils.get(paymentRequest.orderId(), PaymentDto.class);
    }
}
