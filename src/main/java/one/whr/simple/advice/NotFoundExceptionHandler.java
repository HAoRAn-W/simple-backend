package one.whr.simple.advice;

import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleException(NotFoundException ex) {
        return ResponseEntity.ok().body(new MessageResponse(ex.getCode(), ex.getMessage()));
    }
}
