package exception;

public class ConnectedToReservationException extends RuntimeException {
    public ConnectedToReservationException() {
    }

    public ConnectedToReservationException(String message) {
        super(message);
    }

    public ConnectedToReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectedToReservationException(Throwable cause) {
        super(cause);
    }
}
