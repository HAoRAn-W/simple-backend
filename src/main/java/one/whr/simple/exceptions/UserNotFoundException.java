package one.whr.simple.exceptions;

import one.whr.simple.constant.MessageCode;

public class UserNotFoundException extends NotFoundException {
    private final String message;

    public UserNotFoundException(String message) {
        super(MessageCode.USER_NOTFOUND);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
