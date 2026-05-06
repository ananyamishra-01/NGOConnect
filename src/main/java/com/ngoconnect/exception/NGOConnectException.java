package com.ngoconnect.exception;

public class NGOConnectException extends RuntimeException {
    public NGOConnectException(String message) { super(message); }
    public NGOConnectException(String message, Throwable cause) { super(message, cause); }
}
