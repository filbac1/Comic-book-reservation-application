package data;

import entity.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DataSource {
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
}
