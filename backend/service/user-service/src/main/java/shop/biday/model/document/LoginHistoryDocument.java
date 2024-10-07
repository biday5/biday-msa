package shop.biday.model.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "login_history")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistoryDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    @CreatedDate
    @Field(write = Field.Write.ALWAYS)
    private LocalDateTime createdAt;
}
