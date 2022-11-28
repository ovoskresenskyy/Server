package org.example.exceptions;

public class SocketIsNotReadyToGetUserDataException extends RuntimeException {

    public SocketIsNotReadyToGetUserDataException() {
        super("Socket is closed or not connected while user input data.");
    }
}

