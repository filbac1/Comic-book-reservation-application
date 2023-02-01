package data;

import entity.Customer;
import entity.User;
import entity.UserRole;
import exception.DataSourceException;
import javafx.scene.control.Alert;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseAndFileDataSource implements DataSource, Closeable {
    private static final Path USER_FILE = Path.of("dat/user.txt");
    private final Connection connection;

    public DatabaseAndFileDataSource() throws DataSourceException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/db.properties"));

        String dbUrl = properties.getProperty("url");
        String dbUsername = properties.getProperty("username");
        String dbPassoword = properties.getProperty("password");

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassoword);
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public UserRole userStringCompare(String role) {
        if (role.equals("ADMINISTRATION_ROLE")) {
            return UserRole.ADMINISTRATION_ROLE;
        } else if (role.equals("USER_ROLE")) {
            return UserRole.USER_ROLE;
        } else {
            return UserRole.UNKNOWN_ROLE;
        }
    }

    public Set<User> loadAllUsers() {
        Path userFile = USER_FILE;
        Set<User> userSet = new HashSet<>();

        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split(";");

                long id = Long.parseLong(fields[0]);
                String username = fields[1];
                String password = fields[2];
                UserRole role = userRoleDetector(fields[3]);

                User user = new User(id, username, password, role);
                System.out.println(user);
                userSet.add(user);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + userFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userSet;
    }

    public List<Customer> loadAllCustomersFromDatabase() {
        List<Customer> customerList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER");

            while (resultSet.next()) {
                Integer customerID = resultSet.getInt("CUSTOMER_ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");

                Customer helpingCustomer = new Customer(customerID, firstName, lastName);
                customerList.add(helpingCustomer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    public void updateDataForCustomerInDatabase(Customer customer) {
        try {
            PreparedStatement customerUpdateStatement = connection.prepareStatement("UPDATE CUSTOMER SET FIRST_NAME = ?, LAST_NAME = ? WHERE CUSTOMER_ID = ? ");

            customerUpdateStatement.setString(1,customer.firstName());
            customerUpdateStatement.setString(2,customer.lastName());
            customerUpdateStatement.setInt(3,customer.customerID());

            customerUpdateStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewCustomerInDatabase(Customer customer) {
        try {
            PreparedStatement customerUpdateStatement = connection.prepareStatement("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES (?, ?)");

            customerUpdateStatement.setString(1,customer.firstName());
            customerUpdateStatement.setString(2,customer.lastName());

            customerUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOldCustomerInDatabase(Customer customer) {
        try {
            PreparedStatement customerUpdateStatement = connection.prepareStatement("DELETE CUSTOMER WHERE CUSTOMER_ID = ? ");
            customerUpdateStatement.setInt(1,customer.customerID());

            try {
                customerUpdateStatement.executeUpdate();
            } catch (Exception e) {
                var alert = new Alert(Alert.AlertType.ERROR, "Customer is connected to a reservation! Delete the reservation first!");
                alert.setTitle("Error while trying to delete from database!");
                alert.show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<User> readAllUsersFromFile() {
        return loadAllUsers();
    }

    @Override
    public UserRole userRoleDetector(String role) {
        return userStringCompare(role);
    }

    @Override
    public List<Customer> readAllCustomersFromDatabase() {
        return loadAllCustomersFromDatabase();
    }

    @Override
    public void createCustomerInDatabase(Customer customer) {
        createNewCustomerInDatabase(customer);
    }
    @Override
    public void updateCustomerInDatabase(Customer customer) {
        updateDataForCustomerInDatabase(customer);
    }

    @Override
    public void deleteCustomerInDatabase(Customer customer) {
        deleteOldCustomerInDatabase(customer);
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException nothing) {

        }
    }
}



