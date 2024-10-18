package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    private String id;
    private String userId;
    private String bankTranId;
    private String bankCode;
    private String bankName;
    private String accountNum;
    private String accountName;
    private String bankRspCode;
    private Date bankTranDate;
}
