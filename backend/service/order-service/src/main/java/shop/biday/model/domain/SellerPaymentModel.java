package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.PaymentMethod;
import shop.biday.model.entity.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class SellerPaymentModel {
    private Long id;
    private Long paymentId;
    private String transactionId;
    private Long totalAmount;
    private Long fee;
    private Long netAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
