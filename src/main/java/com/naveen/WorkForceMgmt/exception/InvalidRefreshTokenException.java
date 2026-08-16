package com.naveen.WorkForceMgmt.exception;

public class InvalidRefreshTokenException extends RuntimeException {

  public InvalidRefreshTokenException(String message) {
    super(message);
  }
}
