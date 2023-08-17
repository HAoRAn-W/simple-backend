package one.whr.simple.dto.response;


import lombok.Getter;
import one.whr.simple.entity.Avatar;
import one.whr.simple.entity.User;

import java.util.List;

@Getter
public class UserInfoResponse extends MessageResponse {
    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    private Avatar avatar;

    public UserInfoResponse(int code, String message) {
        super(code, message);
    }

    public UserInfoResponse(int code, String message, User user, List<String> roles) {
        super(code, message);
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = roles;
        this.avatar = user.getAvatar();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
