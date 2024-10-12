package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.BrandEntity;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long>, QBrandRepository {

    BrandEntity findByNameIgnoreCase(String name);
}
