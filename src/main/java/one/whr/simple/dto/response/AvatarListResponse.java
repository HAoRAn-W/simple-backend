package one.whr.simple.dto.response;

import one.whr.simple.entity.Avatar;

import java.util.List;

public class AvatarListResponse extends MessageResponse{
    List<Avatar> avatars;
    public AvatarListResponse(int code, String message, List<Avatar> avatars) {
        super(code, message);
        this.avatars = avatars;
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<Avatar> avatars) {
        this.avatars = avatars;
    }
}
