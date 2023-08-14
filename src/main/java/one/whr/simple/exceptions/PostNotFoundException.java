package one.whr.simple.exceptions;

import one.whr.simple.constant.MessageCode;

public class PostNotFoundException extends NotFoundException {
    private final String message;

    public PostNotFoundException(String message) {
        super(MessageCode.POST_NOT_FOUND);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
