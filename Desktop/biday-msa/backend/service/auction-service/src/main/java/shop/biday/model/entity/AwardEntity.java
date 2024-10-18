package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "auction")
@DynamicInsert
@Table(name = "awards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private AuctionEntity auction;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "bided_at", nullable = false)
    private LocalDateTime bidedAt;

    @Column(name = "current_bid", nullable = false)
    private BigInteger currentBid;

    @ColumnDefault("1")
    @Column(name = "count", nullable = false)
    private Long count;
}
