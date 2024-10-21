package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.OrderModel;

import java.time.LocalDateTime;

public interface QOrderRepository {
    OrderModel findByOrderId(Long id);

    Slice<OrderModel> findByUser(String user, String period, LocalDateTime cursor, Pageable pageable);
}
