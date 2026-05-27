package casino.banking.controller.user;

import casino.banking.exceptions.UserNotFoundExeption;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//https://www.rfc-editor.org/rfc/rfc9457.html
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserAdviceController {

    @ExceptionHandler(UserNotFoundExeption.class)
    public ResponseEntity<ProblemDetail> handle(UserNotFoundExeption ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handle(ConstraintViolationException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.badRequest().body(detail);
    }
}
