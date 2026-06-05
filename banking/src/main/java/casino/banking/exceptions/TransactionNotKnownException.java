package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionNotKnownException extends RuntimeException implements ServiceExceptions {

  private final HttpStatus status =  HttpStatus.NOT_FOUND;

  public TransactionNotKnownException(Long id) {
    super("Transaction with id " + id + " not found");
  }
  public TransactionNotKnownException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }
}