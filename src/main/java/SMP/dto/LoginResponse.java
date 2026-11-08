package SMP.dto;

import SMP.enums.Role;

public class LoginResponse {

    private String token;
    private Long userId;
    private String name;
    private Role role;

    public LoginResponse(String token, Long userId, String name, Role role) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}