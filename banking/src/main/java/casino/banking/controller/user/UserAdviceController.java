package casino.banking.controller.user;

import casino.banking.handler.user.CustomerNotFoundExeption;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ProblemDetail;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

//https://www.rfc-editor.org/rfc/rfc9457.html
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserAdviceController {

    @ExceptionHandler(CustomerNotFoundExeption.class)
    public ResponseEntity<ProblemDetail> handle(CustomerNotFoundExeption ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }
}
