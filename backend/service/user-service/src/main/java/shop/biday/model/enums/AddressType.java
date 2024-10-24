package shop.biday.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressType {

    HOME("Home"),
    WORK("Work"),
    OTHER("Other");

    private final String type;

    public String getType() {
        return type;
    }

    public static AddressType fromString(String typeString) {
        for (AddressType type : AddressType.values()) {
            if (type.getType().equalsIgnoreCase(typeString)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for type: " + typeString);
    }
}

