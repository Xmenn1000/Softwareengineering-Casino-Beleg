package casino.banking.controller.transaction;


import casino.banking.exceptions.UserNotFoundExeption;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TransactionController.class)
public class TransactionsAdviceController {

    @ExceptionHandler(UserNotFoundExeption.class)
    public ResponseEntity<ProblemDetail> handle(UserNotFoundExeption ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }
}
