package net.ajkret.gourmet.exception;

public class GameException extends RuntimeException {

  public GameException(String message) {
    super(message);
  }

  public GameException(String message, Throwable e) {
    super(message, e);
  }
}
