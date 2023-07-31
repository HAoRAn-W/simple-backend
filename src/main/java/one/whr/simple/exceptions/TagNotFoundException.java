package one.whr.simple.exceptions;

public class TagNotFoundException extends Exception {
    private final String message;

    public TagNotFoundException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
