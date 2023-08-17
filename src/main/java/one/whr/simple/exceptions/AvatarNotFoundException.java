package one.whr.simple.exceptions;

import one.whr.simple.constant.MessageCode;

public class AvatarNotFoundException extends NotFoundException{

    private final String message;
    public AvatarNotFoundException(String message) {
        super(MessageCode.AVATAR_NOT_FOUND);

        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
