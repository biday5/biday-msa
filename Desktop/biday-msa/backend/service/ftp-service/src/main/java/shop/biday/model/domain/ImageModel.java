package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ImageModel {
    private String id;
    private String originalName;
    private String uploadName;
    private String uploadPath;
    private String uploadUrl;
    private String type;
    private Long referencedId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
