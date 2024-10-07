package shop.biday.model.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "account_verifications")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDocument {

    @Id
    private String id;

    @Field("user_id")
    @Indexed(unique = true)
    private String userId;

    @Field("bank_tran_id")
    private String bankTranId;

    @Field("bank_code")
    private String bankCode;

    @Field("bank_name")
    private String bankName;

    @Field("account_number")
    private String accountNumber;

    @Field("account_name")
    private String accountName;

    @Field("bank_rsp_code")
    private String bankRspCode;

    @Field("bank_tran_date")
    private Date bankTranDate;

    @CreatedDate
    @Field(write = Field.Write.ALWAYS)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
