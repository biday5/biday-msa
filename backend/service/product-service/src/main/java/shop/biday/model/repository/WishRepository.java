package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.WishEntity;

public interface WishRepository extends JpaRepository<WishEntity, Long>, QWishRepository {

}
