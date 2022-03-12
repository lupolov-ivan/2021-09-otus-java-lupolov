package ru.otus.appcontainer.api;

public class ComponentInitializationException extends RuntimeException {

    public ComponentInitializationException() {
        super();
    }

    public ComponentInitializationException(String message) {
        super(message);
    }

    public ComponentInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}