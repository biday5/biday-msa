package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import shop.biday.model.entity.enums.Color;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sub_name", nullable = true)
    private String subName;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "price", nullable = true)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = true)
    private Color color;

    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<SizeEntity> sizes;

    @OneToMany(mappedBy = "product")
    private List<WishEntity> wishes;
}
