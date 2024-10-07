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
public class PaymentCardModel {

    private String issuerName;
    private String issuerCode;
    private Integer installmentPlanMonths;
    private String approveNo;
    private String cardType;
    private boolean isInterestFree;
}
