// package shop.biday.model.repository;
package shop.biday.orderTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// import shop.biday.model.entity.OrderEntity;
import shop.biday.orderTest.*;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, QOrderRepository {

}