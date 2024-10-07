package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.AnnouncementEntity;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementModel {

    private Long id;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnnouncementModel of(final AnnouncementEntity announcement) {
        return AnnouncementModel.builder()
                .id(announcement.getId())
                .userId(announcement.getUserId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .createdAt(announcement.getCreatedAt())
                .updatedAt(announcement.getUpdatedAt())
                .build();
    }
}
