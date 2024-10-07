package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AnnouncementModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.AnnouncementEntity;
import shop.biday.model.repository.AnnouncementRepository;
import shop.biday.service.AnnouncementService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public List<AnnouncementModel> findAll() {
        return announcementRepository.findAll()
                .stream()
                .map(AnnouncementModel::of)
                .toList();
    }

    @Override
    public AnnouncementModel save(String userInfo, AnnouncementModel announcementModel) {
        UserInfoModel userInfoModel = getUserInfoModel(userInfo);

        return AnnouncementModel.of(announcementRepository.save(
                AnnouncementEntity.builder()
                        .userId(userInfoModel.getUserId())
                        .title(announcementModel.getTitle())
                        .content(announcementModel.getContent())
                        .build()));
    }

    @Override
    public AnnouncementModel findById(Long id) {
        return AnnouncementModel.of(announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public boolean deleteById(Long id, String userInfo) {
        getUserInfoModel(userInfo);

        if (!existsById(id)) {
            return false;
        }
        announcementRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return announcementRepository.existsById(id);
    }

    private UserInfoModel getUserInfoModel(String userInfo) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);
        if (!userInfoModel.getUserRole().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new IllegalArgumentException("사용자 정보가 올바르지 않습니다.");
        }
        return userInfoModel;
    }
}
