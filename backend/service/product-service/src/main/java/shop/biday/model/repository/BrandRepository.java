package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long>, QBrandRepository {
    List<BrandEntity> findAll();

    Optional<BrandEntity> findById(Long id);

    BrandEntity findByName(String brandName);

    boolean existsById(Long id);

    BrandEntity save(BrandModel brand);

    void deleteById(Long id);
}
