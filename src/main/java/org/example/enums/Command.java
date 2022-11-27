package org.example.enums;

import java.util.Arrays;

public enum Command {
    SEND_FILE("-file", "(to send file on the server)", true),
    MESSAGE("-msg", "(to send message to all connected clients)", true),
    EXIT("-exit", "(to close application)", true),
    WRONG_INPUT("", "", false);

    private final String name;
    private final String description;
    private final boolean visibility;

    Command(String name, String description, boolean visibility) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public boolean getVisibility(){
        return visibility;
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
