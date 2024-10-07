package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.FaqEntity;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class FaqModel {

    private Long id;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FaqModel of(final FaqEntity faq) {
        return FaqModel.builder()
                .id(faq.getId())
                .userId(faq.getUserId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .build();
    }
}
