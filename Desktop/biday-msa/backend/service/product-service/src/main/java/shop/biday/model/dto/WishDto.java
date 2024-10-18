package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {

    private Long id;
    private ProductResponse product;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
