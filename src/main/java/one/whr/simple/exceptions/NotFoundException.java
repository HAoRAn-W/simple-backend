package one.whr.simple.exceptions;

public class NotFoundException extends Exception{
    public int code;

    public NotFoundException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
