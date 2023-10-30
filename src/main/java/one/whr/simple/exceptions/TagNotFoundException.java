package one.whr.simple.exceptions;

import one.whr.simple.constant.MessageCode;

public class TagNotFoundException extends NotFoundException {
    private final String message;

    public TagNotFoundException(String message) {
        super(MessageCode.TAG_NOT_FOUND);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
