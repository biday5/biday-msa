package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.ProductEntity;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, QProductRepository {
    Optional<ProductEntity> findById(Long id);

    ProductEntity findByName(String name);

    boolean existsById(Long id);

    ProductEntity save(ProductEntity product);

    void deleteById(Long id);
}
