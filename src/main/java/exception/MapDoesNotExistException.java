package exception;

public class MapDoesNotExistException extends Exception {
    public MapDoesNotExistException() {
    }

    public MapDoesNotExistException(String message) {
        super(message);
    }

    public MapDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
