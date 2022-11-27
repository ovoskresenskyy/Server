package org.example.exceptions;

public class UserInputIsNullException extends NullPointerException {

    public UserInputIsNullException() {
        super("User closed application incorrectly. Command is null.");
    }
}
