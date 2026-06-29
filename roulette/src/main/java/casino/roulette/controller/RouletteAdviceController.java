package casino.roulette.controller;

import casino.roulette.exceptions.HttpException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = RouletteController.class)
public class RouletteAdviceController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handle(HttpException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(detail);
    }
}
