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
public class BrandModel {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
