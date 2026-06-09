package casino.banking.controller.user;

import casino.banking.exceptions.HttpException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//https://www.rfc-editor.org/rfc/rfc9457.html
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserAdviceController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handle(HttpException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getHTTPStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }
}