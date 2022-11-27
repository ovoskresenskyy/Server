package org.example.enums;

import java.util.Arrays;

public enum Command {
    SEND_FILE("-file", "(to send file on the server)"),
    MESSAGE("-msg", "(to send message to all connected clients)"),
    EXIT("-exit", "(to close application)");

    private final String name;
    private final String description;

    Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public static Command getFromString(String userInput) throws IllegalArgumentException {
        return Arrays.stream(values())
                .filter(command -> command.name.equalsIgnoreCase(userInput))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown value")); // todo: need to rework
    }

    @Override
    public String toString() {
        return name;
    }
}
