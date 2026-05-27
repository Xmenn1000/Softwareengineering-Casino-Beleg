package casino.banking.exceptions;

public class ModelValidityBreachException extends RuntimeException {
  public ModelValidityBreachException(String message, Throwable cause) {
    super(message, cause);
  }
  public ModelValidityBreachException(String message) {
    super(message);
  }
}
