package org.mai.sn.exceptions;

public class UserNotRegisteredException extends RuntimeException {
    public UserNotRegisteredException(String message) {
        super(message);
    }
}
