package casino.slots.controller;

import casino.slots.exeptions.HttpException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SlotsAdviceController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handle(HttpException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                ex.getHTTPStatus(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getHTTPStatus())
                .body(detail);
    }
}
