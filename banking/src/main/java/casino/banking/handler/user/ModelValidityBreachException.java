package casino.banking.handler.user;

public class ModelValidityBreachException extends RuntimeException {
  public ModelValidityBreachException(String message, Throwable cause) {
    super(message, cause);
  }
  public ModelValidityBreachException(String message) {
    super(message);
  }
}
