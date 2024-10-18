package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelModel {

    private String transactionKey;
    private String cancelReason;
    private String canceledAt;
    private Long cancelAmount;
    private String cancelStatus;
}
