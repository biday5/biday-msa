package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.FaqModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.FaqEntity;
import shop.biday.model.repository.FaqRepository;
import shop.biday.service.FaqService;
import shop.biday.utils.UserInfoUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public List<FaqModel> findAll() {
        return faqRepository.findAll()
                .stream()
                .map(FaqModel::of)
                .toList();
    }

    @Override
    public FaqModel save(String userInfo, FaqModel faqModel) {
        UserInfoModel userInfoModel = getUserInfoModel(userInfo);

        return FaqModel.of(faqRepository.save(FaqEntity.builder()
                .userId(userInfoModel.getUserId())
                .title(faqModel.getTitle())
                .content(faqModel.getContent())
                .build()));
    }

    @Override
    public FaqModel findById(Long id) {
        return FaqModel.of(faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public boolean deleteById(Long id, String userInfo) {
        getUserInfoModel(userInfo);

        if (!existsById(id)) {
            return false;
        }
        faqRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return faqRepository.existsById(id);
    }

    private UserInfoModel getUserInfoModel(String userInfo) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);
        if (!userInfoModel.getUserRole().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new IllegalArgumentException("사용자 정보가 올바르지 않습니다.");
        }
        return userInfoModel;
    }
}
