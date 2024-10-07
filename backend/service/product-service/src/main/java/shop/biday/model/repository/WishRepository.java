package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.WishEntity;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long>, QWishRepository {


}
