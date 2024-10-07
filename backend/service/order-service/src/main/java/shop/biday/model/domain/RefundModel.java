package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class RefundModel {
    private Long id;
    private Long paymentId;
    private Long amount;
    private LocalDateTime refundedAt;
    private PaymentStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
