// package shop.biday.model.repository;
package shop.biday.orderTest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserInfoModel;
// import shop.biday.model.entity.OrderEntity;
// import shop.biday.model.domain.OrderModel;
// import shop.biday.model.repository.OrderRepository;
// import shop.biday.service.OrderService;
import shop.biday.model.repository.PaymentRepository;
import shop.biday.model.repository.ShipperRepository;
import shop.biday.order.*;
import shop.biday.utils.UserInfoUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ShipperRepository shipperRepository;

    private final UserInfoUtils userInfoUtils;

    @Override
    public List<OrderEntity> findAll() {
        log.info("Find all orders");
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity findById(Long id) {
        log.info("Find order by id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found for id: {}", id);
                    return new IllegalArgumentException("유효하지 않은 데이터입니다.");
                });
    }

    @Override
    public ResponseEntity<OrderEntity> save(String userInfoHeader, OrderDto order) {
        log.info("Saving order: {}", order);
        if(validateUser(userInfoHeader).isEmpty()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            OrderEntity savedOrder = createOrderEntity(order);

        }

//        return validateUser(userInfoHeader)
//                .map(t -> {
//                    OrderEntity savedOrder = createOrderEntity(order);
//                    log.debug("Order saved successfully: {}", savedOrder.getId());
//                    return new ResponseEntity<>(orderRepository.save(savedOrder), HttpStatus.OK);
//                }
//                .orElseGet(() -> {
//                    log.error("Save Order failed: User does not have permission");
//                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//                });
    }

    @Override
    public ResponseEntity<OrderModel> findByOrderId(String userInfoHeader, Long orderId) {
        log.info("Find User {} Order by Id: {}", userInfoHeader, orderId);
        return validateUser(userInfoHeader)
                .flatMap(uid -> orderRepository.findById(orderId)
                        .filter(order -> {
                            boolean isAuthorized = order.getUserId().equals(uid);
                            if (isAuthorized) {
                                log.info("User {} is authorized for order id {}", uid, orderId);
                            } else {
                                log.warn("User {} is not authorized for order id {}", uid, orderId);
                            }
                            return isAuthorized;
                        })
                        .map(order -> {
                            log.info("Order found for User {}: {}", uid, orderId);
                            return ResponseEntity.ok(orderRepository.findByOrderId(orderId));
                        }))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    String userId = userInfoUtils.extractUserInfo(userInfoHeader).getUserId();
                    if (orderRepository.findById(orderId).isPresent()) {
                        log.error("User {} is not authorized for order id {}", userId, orderId);
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    } else {
                        log.error("Order not found for order id {}", orderId);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                }).getBody();
    }

    @Override
    public ResponseEntity<Slice<OrderModel>> findByUser(String userInfoHeader, String period, LocalDateTime cursor, Pageable pageable) {
        log.info("Finding orders for User: {}", userInfoHeader);
        return validateUser(userInfoHeader)
                .map(uid -> {
                    log.info("Valid user {} found, fetching orders.", uid);
                    Slice<OrderModel> orders = orderRepository.findByUser(uid, period, cursor, pageable);

                    if (orders.isEmpty()) {
                        log.warn("No orders found for user {} with period {}", uid, period);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Slice<OrderModel>) null);
                    }

                    return ResponseEntity.ok(orders);
                })
                .orElseGet(() -> {
                    log.error("Invalid user ID: {}", userInfoUtils.extractUserInfo(userInfoHeader).getUserId());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Slice<OrderModel>) null);
                });
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserId())
                .filter(uid -> {
                    boolean isValid = !uid.isEmpty();
                    if (!isValid) {
                        log.error("Invalid user ID: {}", uid);
                    }
                    return isValid;
                });
    }

    private OrderEntity createOrderEntity(OrderDto order) {
        log.info("Creating order entity for order: {}", order.getOrderId());
        return OrderEntity.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .address(order.getAddress())
                .auctionId(order.getAuctionId())
                .awardId(order.getAwardId())
                .awardedAt(order.getAwardedAt())
                .awardBid(order.getAwardBid())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .size(order.getSize())
//                .payment(paymentRepository.findById(order.getPaymentId()))
                .payment(order.getPaymentId())
                .shipper(order.getShipperId())
                .build();
    }
}