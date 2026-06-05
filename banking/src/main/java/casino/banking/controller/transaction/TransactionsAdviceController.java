package casino.banking.controller.transaction;


import casino.banking.exceptions.TransactionNotKnownException;
import casino.banking.exceptions.UserNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TransactionController.class)
public class TransactionsAdviceController {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ProblemDetail> handle(UserNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }

    @ExceptionHandler(TransactionNotKnownException.class)
    public ResponseEntity<ProblemDetail> handle(TransactionNotKnownException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }
}