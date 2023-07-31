package one.whr.simple.exceptions;

public class UserNotFoundException extends Exception {
    private final String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
