package shop.biday.model.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.mapping.Field.Write;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bids")
public class BidDocument {

    @Id
    private String id;

    @Field(write = Write.ALWAYS)
    private Long auctionId;

    @Field(write = Write.ALWAYS)
    private String userId;

    @Field(write = Write.ALWAYS, targetType = FieldType.DECIMAL128)
    private BigInteger currentBid;

    @Builder.Default
    @Field(write = Write.ALWAYS)
    private boolean award = false;

    @CreatedDate
    @Field(write = Write.ALWAYS)
    private LocalDateTime bidedAt;

    @CreatedDate
    @Field(write = Write.ALWAYS)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field
    private LocalDateTime updatedAt;
}
