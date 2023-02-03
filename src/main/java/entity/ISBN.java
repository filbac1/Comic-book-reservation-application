package entity;

import java.util.Objects;

public class ISBN<T extends String> {

    private T ISBNNumber;

    public ISBN(T ISBNNumber) {
        this.ISBNNumber = ISBNNumber;
    }

    public T getISBNNumber() {
        return ISBNNumber;
    }

    public void setISBNNumber(T ISBNNumber) {
        this.ISBNNumber = ISBNNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBNNumber);
    }

    public ISBN setISBNNumberBuilder(T obj)
    {
        this.ISBNNumber = obj;
        return this;
    }

}
