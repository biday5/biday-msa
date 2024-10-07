package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.SizeEntity;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Long>, QSizeRepository {

}
