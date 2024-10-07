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
public class WishModel {
    private Long id;
    private Long userId;
    private Long productId;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
