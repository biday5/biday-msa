// package shop.biday.service;
package shop.biday.orderTest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
// import shop.biday.model.domain.OrderModel;
// import shop.biday.model.entity.OrderEntity;
import shop.biday.orderTest.*;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    List<OrderEntity> findAll();

    OrderEntity findById(Long id);

    ResponseEntity<OrderEntity> save(String userInfoHeader, OrderDto order);

    ResponseEntity<OrderModel> findByOrderId(String userInfoHeader, Long orderId);

    ResponseEntity<Slice<OrderModel>> findByUser(String userInfoHeader, String period, LocalDateTime cursor, Pageable pageable);
}
