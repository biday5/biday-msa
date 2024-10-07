package shop.biday.oauth2.OauthDto;

import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {

        this.attribute = attribute; // 직접 attribute를 저장합니다.
    }
//    public NaverResponse(Map<String, Object> attribute) {
//        this.attribute = (Map<String, Object>) attribute.get("response");
//    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getBirthyear() {
        Object birthyearObj = attribute.get("birthyear");
        return birthyearObj != null ? birthyearObj.toString() : null;
    }

    @Override
    public String getMobile() {
        return attribute.get("mobile").toString();
    }
}
