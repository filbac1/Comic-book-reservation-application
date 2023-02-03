package exception;

public class NoSuchComicID extends NullPointerException{
    public NoSuchComicID() {
    }

    public NoSuchComicID(String s) {
        super(s);
    }
}
