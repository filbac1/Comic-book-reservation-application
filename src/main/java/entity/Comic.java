package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Comic extends Book {

    private Integer comicID;

    private Publishers publisher;
    private ISBN isbn;

    public Comic(String bookName, Integer comicID, Publishers publisher, ISBN isbn) {
        super(bookName);
        this.comicID = comicID;
        this.publisher = publisher;
        this.isbn = isbn;
    }

    public Integer getComicID() {
        return comicID;
    }

    public void setComicID(Integer comicID) {
        this.comicID = comicID;
    }

    public Publishers getPublisher() {
        return publisher;
    }

    public void setPublisher(Publishers publisher) {
        this.publisher = publisher;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public void setIsbn(ISBN isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comic comic = (Comic) o;
        return Objects.equals(comicID, comic.comicID) && publisher == comic.publisher && Objects.equals(isbn, comic.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), comicID, publisher, isbn);
    }

    @Override
    public String toString() {
        return bookName;
    }

    /**
     * Sets comicID (builder pattern)
     * @param obj
     * @return Comic
     */

    public Comic setComicIDBuilder(Integer obj)
    {
        this.comicID = obj;
        return this;
    }

    /**
     * Sets publisher of comic (builder pattern)
     * @param obj
     * @return Comic
     */

    public Comic setComicPublisherBuilder(Publishers obj)
    {
        this.publisher = obj;
        return this;
    }

    /**
     * Sets ISBN of comic (builder pattern)
     * @param obj
     * @return Comic
     */

    public Comic setISBNBuilder(ISBN obj)
    {
        this.isbn = obj;
        return this;
    }
}
