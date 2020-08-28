package net.ajkret.gourmet.exception;

public class RepositoryException extends RuntimeException {

  public RepositoryException(String message) {
    super(message);
  }

  public RepositoryException(String message, Throwable e) {
    super(message, e);
  }
}
