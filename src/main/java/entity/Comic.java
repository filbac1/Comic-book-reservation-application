package entity;

public class Comic extends Book {

    private Publishers publisher;
    private ISBN isbn;

    public Comic(String bookName, Publishers publisher, ISBN isbn) {
        super(bookName);

    }
}
