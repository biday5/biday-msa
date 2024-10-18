package shop.biday.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
    CHARCOAL("CHARCOAL"),
    GRAY("GRAY"),
    BEIGE("BEIGE"),
    OLIVE("OLIVE"),
    PURPLE("PURPLE"),
    IVORY("IVORY"),
    GREEN("GREEN"),
    MELANGE("MELANGE"),
    BLUE("BLUE"),
    WHITE("WHITE"),
    CREAM("CREAM"),
    YELLOW("YELLOW"),
    BROWN("BROWN"),
    RED("RED"),
    NAVY("NAVY"),
    PINK("PINK"),
    ORANGE("ORANGE"),
    KHAKI("KHAKI"),
    BURGUNDY("BURGUNDY"),
    BLACK("BLACK");

    private final String color;

    public String getColor() {
        return color;
    }

    public static Color fromString(String colorString) {
        for (Color c : Color.values()) {
            if (c.getColor().equals(colorString)) {
                return c;
            }
        }
        throw new IllegalArgumentException("No enum constant for color: " + colorString);
    }
}
