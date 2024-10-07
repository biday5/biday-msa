package shop.biday.service;

import shop.biday.model.domain.AnnouncementModel;

import java.util.List;

public interface AnnouncementService {

    List<AnnouncementModel> findAll();

    AnnouncementModel save(String userInfo, AnnouncementModel announcementModel);

    AnnouncementModel findById(Long id);

    boolean deleteById(Long id, String userInfo);

    boolean existsById(Long id);
}
