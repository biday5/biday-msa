package shop.biday.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.biday.exception.PaymentException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    READY("READY", "결제 인증 대기"),
    IN_PROGRESS("IN_PROGRESS", "결제 진행중"),
    DONE("DONE", "결제 승인"),
    CANCELED("CANCELED", "결제 취소"),
    ABORTED("ABORTED", "결제 승인 실패"),
    EXPIRED("EXPIRED", "결제 유효시간 초과");

    private final String status;
    private final String message;

    public static PaymentStatus fromStatus(String status) {
        return Arrays.stream(PaymentStatus.values())
                .filter(p -> p.status.equals(status))
                .findFirst()
                .orElseThrow(PaymentException::new);
    }
}
