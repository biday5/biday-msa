package shop.biday.oauth2.jwt;

public class JwtClaims {
    private String userId;
    private String role;
    private String name;

    public JwtClaims(String userId, String role, String name) {
        this.userId = userId;
        this.role = role;
        this.name = name;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}

