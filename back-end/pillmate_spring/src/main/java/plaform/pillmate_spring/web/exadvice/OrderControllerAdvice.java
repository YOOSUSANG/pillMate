package plaform.pillmate_spring.web.exadvice;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import plaform.pillmate_spring.web.dto.error.ErrorResult;

@RestControllerAdvice(assignableTypes = OrderControllerAdvice.class)
public class OrderControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> HttpClientErrorException(HttpClientErrorException e) {
        ErrorResult errorResult = new ErrorResult(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> BadRequestException(BadRequestException e) {
        ErrorResult errorResult = new ErrorResult(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
