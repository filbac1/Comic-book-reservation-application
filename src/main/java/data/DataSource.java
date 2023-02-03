package data;

import entity.*;
import exception.MapDoesNotExistException;
import main.controllers.ReservationSearchController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DataSource {
    List<Change> loadAllChanges();

    void writeChanges(List<Change> changeList);

    Set<User> readAllUsersFromFile();
    UserRole userRoleDetector(String role);

    List<Customer> readAllCustomersFromDatabase();

    void createCustomerInDatabase(Customer customer);
    void updateCustomerInDatabase(Customer customer);

    void deleteCustomerInDatabase(Customer customer);

    List<Comic> readAllComicsFromDatabase();

    void createComicInDatabase(Comic comic);

    void deleteComicInDatabase(Comic comic);

    void updateComicInDatabase(Comic comic);

    List<Reservation> readAllReservationsFromDatabase();

    Optional<Comic> readComicWhereID (Integer ID);
    Optional<Customer> readCustomerWhereID (Integer ID);

    void createReservationInDatabase(Reservation reservation);

    void deleteReservationInDatabase(Reservation reservation);

    void updateReservationInDatabase(Reservation reservation);

    boolean comicConnectedToReservation(Comic comic);
    boolean customerConnectedToReservation(Customer customer);

    Map<Integer, Integer> getNumberOfComics() throws MapDoesNotExistException;

}
