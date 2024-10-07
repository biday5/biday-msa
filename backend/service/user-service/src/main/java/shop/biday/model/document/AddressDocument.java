package shop.biday.model.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import shop.biday.model.enums.AddressType;

import java.time.LocalDateTime;

@Document(collection = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDocument {
    @Id
    private String id; // In MongoDB, the ID is usually a String

    @Field("user_id")
    @Indexed
    private String userId;

    @Field("street_address")
    private String streetAddress;

    @Field("detail_address")
    private String detailAddress;

    @Field("zipcode")
    private String zipcode;

    @Field("type")
    private AddressType type;

    @Field("pick")
    private boolean pick;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
