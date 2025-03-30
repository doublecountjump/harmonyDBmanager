package harmony.dbproject.handler;


import harmony.dbproject.error.ErrorException;
import harmony.dbproject.error.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    private String ErrorExceptionHandler(ErrorException e){
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());

        return errorResponse.toString();
    }
}
