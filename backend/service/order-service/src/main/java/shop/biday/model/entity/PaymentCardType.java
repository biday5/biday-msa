package shop.biday.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.biday.exception.PaymentException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentCardType {

    LOTTE("71", "롯데카드"),
    SAMSUNG("51", "삼성카드"),
    SHINHAN("41", "신한카드"),
    WOORI("33", "우리BC카드"),
    KAKAOBANK("15", "카카오뱅크"),
    KBANK("3A", "케이뱅크"),
    TOSSBANK("24", "토스뱅크"),
    HANA("21", "하나카드"),
    HYUNDAI("61", "현대카드"),
    KOOKMIN("11", "KB국민카드"),
    NONGHYEOP("91", "NH농협카드");

    private final String code;
    private final String name;

    public static PaymentCardType getByCode(String code) {
        return Arrays.stream(PaymentCardType.values())
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElseThrow(PaymentException::new);
    }
}
