package one.whr.simple.dto.response;


import one.whr.simple.entity.User;

import java.util.List;

public class UserInfoResponse extends MessageResponse {
    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    private String avatarUrl;

    public UserInfoResponse(int code, String message) {
        super(code, message);
    }

    public UserInfoResponse(int code, String message, User user, List<String> roles) {
        super(code, message);
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = roles;
        this.avatarUrl = user.getAvatarUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
