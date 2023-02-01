package data;

import entity.Customer;
import entity.User;
import entity.UserRole;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

public interface DataSource {
    Set<User> readAllUsersFromFile();
    UserRole userRoleDetector(String role);

    List<Customer> readAllCustomersFromDatabase();

    void createCustomerInDatabase(Customer customer);
    void updateCustomerInDatabase(Customer customer);

    void deleteCustomerInDatabase(Customer customer);
}
