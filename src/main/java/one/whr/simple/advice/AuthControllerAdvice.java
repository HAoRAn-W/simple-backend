package one.whr.simple.advice;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_NOTFOUND, ex.getMessage()));
    }
}
