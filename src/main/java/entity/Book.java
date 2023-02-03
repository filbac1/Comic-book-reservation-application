package entity;

import java.util.Objects;

public abstract class Book {

    public String bookName;

    public Book(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookName, book.bookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookName);
    }

    /**
     * Sets book name (builder pattern)
     * @param name
     * @return
     */
    public Book setBookNameBuilder(String name)
    {
        this.bookName = name;
        return this;
    }
}
