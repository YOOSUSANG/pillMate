package plaform.pillmate_spring.web.exadvice;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import plaform.pillmate_spring.web.controller.PillController;
import plaform.pillmate_spring.web.dto.error.ErrorResult;

@RestControllerAdvice(assignableTypes = PillController.class)
public class PillControllerAdvice {


    @ExceptionHandler
    public ResponseEntity<ErrorResult> BadRequestException(BadRequestException e) {
        ErrorResult errorResult = new ErrorResult(e.getMessage(), HttpStatus.BAD_REQUEST);
        //status 변경 가능
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

}
