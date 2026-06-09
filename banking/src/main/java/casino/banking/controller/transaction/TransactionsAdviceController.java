package casino.banking.controller.transaction;


import casino.banking.exceptions.HttpException;
import casino.banking.exceptions.user.UserNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TransactionController.class)
public class TransactionsAdviceController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handle(HttpException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getHTTPStatus(), ex.getMessage());
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }
}