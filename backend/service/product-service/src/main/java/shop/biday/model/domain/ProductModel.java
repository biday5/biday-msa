package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Long id;
    private String brand;
    private String category;
    private String name;
    private String subName;
    private String productCode;
    private Long price;
    private String color;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long wishes;
//    private ImageModel image;
    @Builder.Default
    private Set<SizeModel> sizes = new HashSet<>();
}
