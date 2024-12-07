package informational_systems.lab1.items;

public class LoginResponse {
    private String token;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LoginResponse(String token, int userId) {
        this.token = token;
        this.userId = userId;
    }

    // Геттер для токена
    public String getToken() {
        return token;
    }
}