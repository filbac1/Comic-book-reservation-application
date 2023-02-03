package entity;

import java.util.List;
import java.util.Objects;

public class Reservation<T extends Customer, V extends Comic> {
    private Integer reservationID;
    private T customer;
    private V comic;

    public Reservation(Integer reservationID, T customer, V comic) {
        this.reservationID = reservationID;
        this.customer = customer;
        this.comic = comic;
    }

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public T getCustomer() {
        return customer;
    }

    public void setCustomer(T customer) {
        this.customer = customer;
    }

    public V getComic() {
        return comic;
    }

    public void setComic(V comic) {
        this.comic = comic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation<?, ?> that = (Reservation<?, ?>) o;
        return Objects.equals(reservationID, that.reservationID) && Objects.equals(customer, that.customer) && Objects.equals(comic, that.comic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationID, customer, comic);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=" + reservationID +
                ", customer=" + customer +
                ", comic=" + comic +
                '}';
    }

    public Reservation setReservationIDBuilder(Integer obj)
    {
        this.reservationID = obj;
        return this;
    }

    public Reservation setReservationCustomerBuilder(T obj)
    {
        this.customer = obj;
        return this;
    }

    public Reservation setReservationComicBuilder(V obj)
    {
        this.comic = obj;
        return this;
    }
}
