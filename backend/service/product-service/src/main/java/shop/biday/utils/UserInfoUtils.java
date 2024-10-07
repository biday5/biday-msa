package shop.biday.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.UserInfoModel;

@Component
public class UserInfoUtils {
    private  final ObjectMapper objectMapper = new ObjectMapper();

    public UserInfoModel extractUserInfo(String userInfoHeader) {
        if (userInfoHeader == null || userInfoHeader.isEmpty()) {
            throw new IllegalArgumentException("UserInfo 헤더가 비어있거나 유효하지 않습니다.");
        }

        try {
            return objectMapper.readValue(userInfoHeader, UserInfoModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("유효하지 않은 UserInfo 형식", e);
        }
    }
}
