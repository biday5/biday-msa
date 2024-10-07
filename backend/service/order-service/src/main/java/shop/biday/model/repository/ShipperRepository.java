package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;

import java.util.List;
import java.util.Optional;

public interface ShipperRepository extends JpaRepository<ShipperEntity, Long> {

    List<ShipperEntity> findAll();

    Optional<ShipperEntity> findById(Long id);

    boolean existsById(Long id);

    ShipperEntity save(ShipperModel brand);

    void deleteById(Long id);
}
