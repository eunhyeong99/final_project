package com.sparta.exception;

public class BaseException extends RuntimeException {
  private final ErrorCode errorCode;

  public BaseException(final ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public int getStatusCode() {
    return errorCode.getStatusCode();
  }
}
