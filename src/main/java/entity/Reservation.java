package entity;

import java.util.List;
import java.util.Objects;

public class Reservation<T extends Customer, V extends Comic> {
    private T customer;
    private List<V> comics;

    public Reservation(T customer, List<V> comics) {
        this.customer = customer;
        this.comics = comics;
    }

    public T getCustomer() {
        return customer;
    }

    public void setCustomer(T customer) {
        this.customer = customer;
    }

    public List<V> getComics() {
        return comics;
    }

    public void setComics(List<V> comics) {
        this.comics = comics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation<?, ?> that = (Reservation<?, ?>) o;
        return Objects.equals(customer, that.customer) && Objects.equals(comics, that.comics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, comics);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", comics=" + comics +
                '}';
    }
}
