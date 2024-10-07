package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.AnnouncementEntity;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
}
