package one.whr.simple.exceptions;

public class CategoryNotFoundException extends Exception{
    private String message;
    public CategoryNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
