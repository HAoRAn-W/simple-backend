package one.whr.simple.exceptions;

public class PostNotFoundException extends Exception {
    private final String message;

    public PostNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
