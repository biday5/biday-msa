package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.AwardEntity;

@Repository
public interface AwardRepository extends JpaRepository<AwardEntity, Long>, QAwardRepository {

}