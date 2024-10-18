package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.ShipperEntity;

public interface ShipperRepository extends JpaRepository<ShipperEntity, Long> {

}
