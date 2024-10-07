package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeModel {
    private Long id;
    private String sizeProduct;
    private String size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
