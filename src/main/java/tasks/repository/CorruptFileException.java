package tasks.repository;

public class CorruptFileException extends RuntimeException {
    public CorruptFileException(String message) {
        super(message);
    }
}
