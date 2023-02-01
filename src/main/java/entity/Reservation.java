package entity;

import java.util.List;
import java.util.Objects;

public class Reservation {
    private Integer reservationID;
    private Customer customer;
    private Comic comic;

    public Reservation(Integer reservationID, Customer customer, Comic comic) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
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
}
