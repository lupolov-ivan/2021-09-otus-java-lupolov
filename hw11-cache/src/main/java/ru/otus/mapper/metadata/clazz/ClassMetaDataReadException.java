package ru.otus.mapper.metadata.clazz;

public class ClassMetaDataReadException extends RuntimeException {

    public ClassMetaDataReadException() {
        super();
    }

    public ClassMetaDataReadException(String message) {
        super(message);
    }

    public ClassMetaDataReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassMetaDataReadException(Throwable cause) {
        super(cause);
    }
}
