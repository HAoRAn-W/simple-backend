package one.whr.simple.exceptions;

import lombok.Getter;

public class InfoMismatchException extends Exception {
    @Getter
    private int code;

    private String message;
    public InfoMismatchException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
