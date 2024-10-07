package shop.biday.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Size {
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL"),
    ONE("ONE");

    private final String size;

    public String getSize() {
        return size;
    }

    public static Size fromString(String roleString) {
        for (Size s : Size.values()) {
            if (s.getSize().equals(roleString)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant for role: " + roleString);
    }
}
