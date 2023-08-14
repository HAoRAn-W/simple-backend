package one.whr.simple.exceptions;

import one.whr.simple.constant.MessageCode;

public class CategoryNotFoundException extends NotFoundException {
    private final String message;

    public CategoryNotFoundException(String message) {
        super(MessageCode.CATEGORY_NOT_FOUND);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
