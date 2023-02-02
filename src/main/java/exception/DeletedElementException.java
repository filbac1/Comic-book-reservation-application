package exception;

public class DeletedElementException extends NullPointerException{
    public DeletedElementException() {
    }

    public DeletedElementException(String s) {
        super(s);
    }
}
