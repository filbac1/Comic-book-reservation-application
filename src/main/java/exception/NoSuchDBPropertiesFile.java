package exception;

public class NoSuchDBPropertiesFile extends Exception {
    public NoSuchDBPropertiesFile() {
    }

    public NoSuchDBPropertiesFile(String message) {
        super(message);
    }

    public NoSuchDBPropertiesFile(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchDBPropertiesFile(Throwable cause) {
        super(cause);
    }
}
