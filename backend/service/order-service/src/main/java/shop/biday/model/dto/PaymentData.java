package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PaymentData {
    private Long awardId;
    private String paymentKey;
    private Long amount;
    private String orderId;
    private LocalDateTime approvedAt;
}
