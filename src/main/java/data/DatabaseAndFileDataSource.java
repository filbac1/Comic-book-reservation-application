package data;

import entity.*;
import exception.DataSourceException;
import exception.MapDoesNotExistException;
import exception.NoSuchDBPropertiesFile;
import javafx.scene.control.Alert;
import main.HelloApplication;
import main.controllers.ReservationSearchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public non-sealed class DatabaseAndFileDataSource implements DataSource, Closeable {
    private static final Path USER_FILE = Path.of("dat/user.txt");
    private static final Path CHANGES_FILE = Path.of("dat/changes.dat");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseAndFileDataSource.class);


    public DatabaseAndFileDataSource() throws DataSourceException, IOException, NoSuchDBPropertiesFile {
        Properties properties = new Properties();

        properties.load(new FileReader("src/main/resources/db.properties"));

        String dbUrl = properties.getProperty("url");
        String dbUsername = properties.getProperty("username");
        String dbPassoword = properties.getProperty("password");

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassoword);
        } catch (SQLException e) {
            logger.error("Problem with SQL loading...");
            throw new DataSourceException(e);

        }
    }

    // FILE FUNCTIONS

    /**
     * Compares users administration roles
     * @param role
     * @return UserRole enum
     */
    public UserRole userStringCompare(String role) {
        if (role.equals("ADMINISTRATION_ROLE")) {
            return UserRole.ADMINISTRATION_ROLE;
        } else if (role.equals("USER_ROLE")) {
            return UserRole.USER_ROLE;
        } else {
            return UserRole.UNKNOWN_ROLE;
        }
    }

    /**
     * Loads all users from .txt file
     * @return Set of Users (every User is unique)
     */
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
                userSet.add(user);
            }
        } catch (FileNotFoundException e) {
            logger.error("Problem with file...");
            throw new RuntimeException("File not found: " + userFile);
        } catch (IOException e) {
            logger.error("IOE exception!");
            throw new RuntimeException(e);
        }
        return userSet;
    }

    /**
     * Loads all changes made in program from binary file
     * @return List of class Change
     */
    @Override
    public synchronized List<Change> loadAllChanges() {
        Path changeFile = CHANGES_FILE;
        List<Change> changeList = new ArrayList<>();

        try (Scanner scanner = new Scanner(changeFile)) {
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split(";");

                String objectChanged = String.valueOf(fields[0]);
                String oldValue = String.valueOf(fields[1]);
                String newValue = String.valueOf(fields[2]);
                User user = getUserFromField(fields[3]);
                LocalDateTime localDateTime = LocalDateTime.parse(fields[4], DATE_TIME_FORMAT);

                Change change = new Change(objectChanged, oldValue, newValue, user, localDateTime);
                changeList.add(change);
            }
        } catch (FileNotFoundException e) {
            logger.error("Problem with file...");
            throw new RuntimeException("File not found: " + changeFile);
        } catch (IOException e) {
            logger.error("IOE exception!");
            throw new RuntimeException(e);
        }
        return changeList;
    }

    /**
     * Synchrnozied method for writing all changes in binary file
     * @param changeList
     */

    @Override
    public synchronized void writeChanges(List<Change> changeList) {
        var out = changeList.stream()
                .map(p -> "%s;%s;%s;%s;%s".formatted(
                        p.getObjectChanged(),
                        p.getOldValue(),
                        p.getNewValue(),
                        p.getUser().toString(),
                        p.getLocalDateTime().format(DATE_TIME_FORMAT)
                )).collect(Collectors.joining("\n"));

        try {
            Files.writeString(CHANGES_FILE, out);
        } catch (IOException e) {
            logger.error("IOE exception!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets User from field with String as param
     * @param field
     * @return User
     */

    private User getUserFromField(String field) {
        Set<User> userSet = loadAllUsers();
        List<User> userList = new ArrayList<>(userSet);

        for (User u : userList) {
            if (u.getId().toString().equals(field)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Loads all customers from DB
     * @return List of Customers
     */

    // DATABASE FUNCTIONS
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
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
        return customerList;
    }

    /**
     * Updates customer in DB
     * @param customer
     */
    public void updateDataForCustomerInDatabase(Customer customer) {
        try {
            PreparedStatement customerUpdateStatement = connection.prepareStatement("UPDATE CUSTOMER SET FIRST_NAME = ?, LAST_NAME = ? WHERE CUSTOMER_ID = ? ");

            customerUpdateStatement.setString(1,customer.firstName());
            customerUpdateStatement.setString(2,customer.lastName());
            customerUpdateStatement.setInt(3,customer.customerID());

            customerUpdateStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates new customer in DB
     * @param customer
     */

    private void createNewCustomerInDatabase(Customer customer) {
        try {
            PreparedStatement customerUpdateStatement = connection.prepareStatement("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES (?, ?)");

            customerUpdateStatement.setString(1,customer.firstName());
            customerUpdateStatement.setString(2,customer.lastName());

            customerUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes customer in DB
     * @param customer
     */

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
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all comics from DB
     * @return List of Comics
     */

    private List<Comic> loadAllComicsFromDatabase() {
        List<Comic> comicList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM COMIC");

            while (resultSet.next()) {
                Integer comicID = resultSet.getInt("COMIC_ID");
                String stringISBN = resultSet.getString("ISBN");
                String publisher = resultSet.getString("PUBLISHER");
                String comicName = resultSet.getString("COMIC_NAME");

                Publishers enumPublisher = publisherHelper(publisher);
                ISBN<String> ISBN = new ISBN<>(stringISBN);

                Comic helpingComic = new Comic(comicName, comicID, enumPublisher, ISBN);
                comicList.add(helpingComic);
            }
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
        return comicList;

    }

    /**
     * Compares string with Publisher enum
     * @param publisher
     * @return Publisher value of equal String
     */

    private Publishers publisherHelper(String publisher) {
        for (Publishers p : Publishers.values()) {
            if (publisher.compareTo(p.toString()) == 0) {
                return p;
            }
        }
        return null;
    }

    /**
     * Creates new comic in DB
     * @param comic
     */

    private void createNewComicInDatabase(Comic comic) {
        try {
            PreparedStatement comicUpdateStatement = connection.prepareStatement("INSERT INTO COMIC(ISBN, PUBLISHER, COMIC_NAME) VALUES (?, ?, ?)");

            comicUpdateStatement.setString(1, comic.getIsbn().getISBNNumber());
            comicUpdateStatement.setString(2, comic.getPublisher().name());
            comicUpdateStatement.setString(3, comic.getBookName());

            comicUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes comic in DB
     * @param selectedComic
     */

    private void deleteOldComicInDatabase(Comic selectedComic) {
        try {
            PreparedStatement comicUpdateStatement = connection.prepareStatement("DELETE COMIC WHERE COMIC_ID = ? ");
            comicUpdateStatement.setInt(1, selectedComic.getComicID());

            try {
                comicUpdateStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                var alert = new Alert(Alert.AlertType.ERROR, "Comic is connected to a reservation! Delete the reservation first!");
                alert.setTitle("Error while trying to delete from database!");
                alert.show();
            }
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates comic in DB
     * @param comic
     */

    private void updateDataForComicInDatabase(Comic comic) {
        try {
            PreparedStatement comicUpdateStatement = connection.prepareStatement("UPDATE COMIC SET ISBN = ?, PUBLISHER = ?, COMIC_NAME = ? WHERE COMIC_ID = ? ");

            comicUpdateStatement.setString(1, comic.getIsbn().getISBNNumber());
            comicUpdateStatement.setString(2, comic.getPublisher().name());
            comicUpdateStatement.setString(3, comic.getBookName());
            comicUpdateStatement.setInt(4, comic.getComicID());

            comicUpdateStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all reservations in DB
     * @return
     */

    private List<Reservation> loadAllReservationsFromDatabase() {
        List<Reservation> reservationList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM RESERVATION");

            while (resultSet.next()) {
                Integer reservationID = resultSet.getInt("RESERVATION_ID");
                Integer customerID = resultSet.getInt("CUSTOMER_ID");
                Integer comicID = resultSet.getInt("COMIC_ID");

                Customer customer = readCustomerWhereID(customerID).get();
                Comic comic = readComicID(comicID).get();

                Reservation helpingReservation = new Reservation(reservationID, customer, comic);
                reservationList.add(helpingReservation);
            }
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
        return reservationList;
    }

    /**
     * Returns customer by getting his ID if he exists
     * @param ID
     * @return Optional of customer
     */

    private Optional<Customer> readCustomerID(Integer ID) {
        try {
            PreparedStatement customerStatement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?");
            customerStatement.setInt(1, ID);

            ResultSet resultSet = customerStatement.executeQuery();

            while (resultSet.next()) {
                Integer customerID = resultSet.getInt("CUSTOMER_ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");

                Customer helpingCustomer = new Customer(customerID, firstName, lastName);
                return Optional.of(helpingCustomer);
            }
        } catch (SQLException ex) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(ex);
        }

        return Optional.empty();
    }

    /**
     * Returns comic by id if it exists in DB
     * @param ID
     * @return Optional Comic
     */

    private Optional<Comic> readComicID(Integer ID) {
        try {
            PreparedStatement comicStatement = connection.prepareStatement("SELECT * FROM COMIC WHERE COMIC_ID = ?");
            comicStatement.setInt(1, ID);

            ResultSet resultSet = comicStatement.executeQuery();

            while (resultSet.next()) {
                Integer comicID = resultSet.getInt("COMIC_ID");
                String stringISBN = resultSet.getString("ISBN");
                String publisher = resultSet.getString("PUBLISHER");
                String comicName = resultSet.getString("COMIC_NAME");

                Publishers enumPublisher = publisherHelper(publisher);
                ISBN<String> ISBN = new ISBN<>(stringISBN);

                Comic helpingComic = new Comic(comicName, comicID, enumPublisher, ISBN);

                return Optional.of(helpingComic);
            }
        } catch (SQLException ex) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(ex);
        }

        return Optional.empty();
    }

    /**
     * Creates new reservation in DB
     * @param reservation
     */
    private void createNewReservationInDatabase(Reservation reservation) {
        try {
            PreparedStatement reservationUpdateStatement = connection.prepareStatement("INSERT INTO RESERVATION(CUSTOMER_ID, COMIC_ID) VALUES (?, ?)");

            reservationUpdateStatement.setInt(1, reservation.getCustomer().customerID());
            reservationUpdateStatement.setInt(2, reservation.getComic().getComicID());

            reservationUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes reservation in DB
     * @param selectedReservation
     */

    private void deleteOldReservationInDatabase(Reservation selectedReservation) {
        try {
            PreparedStatement reservationUpdateStatement = connection.prepareStatement("DELETE RESERVATION WHERE RESERVATION_ID = ? ");
            reservationUpdateStatement.setInt(1, selectedReservation.getReservationID());

            try {
                reservationUpdateStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                var alert = new Alert(Alert.AlertType.ERROR, "Fatal error");
                alert.setTitle("Error while trying to delete reservation from database!");
                alert.show();
            }
        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates reservation in DB
     * @param reservation
     */

    private void updateDataForReservationInDatabase(Reservation reservation) {
        try {
            PreparedStatement reservationUpdateStatement = connection.prepareStatement("UPDATE RESERVATION SET CUSTOMER_ID = ?, COMIC_ID = ? WHERE RESERVATION_ID = ? ");

            reservationUpdateStatement.setInt(1, reservation.getCustomer().customerID());
            reservationUpdateStatement.setInt(2, reservation.getComic().getComicID());
            reservationUpdateStatement.setInt(3, reservation.getReservationID());

            reservationUpdateStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Problem with SQL or connection...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls loadAllUsers() and loads them from DB
     * @return Set of Users(unique)
     */

    @Override
    public Set<User> readAllUsersFromFile() {
        return loadAllUsers();
    }

    /**
     * calls userStringCompare
     * @param role
     * @return UserRole
     */

    @Override
    public UserRole userRoleDetector(String role) {
        return userStringCompare(role);
    }

    /**
     * Calls loadAllCustomersFromDatabase()
     * @return List<Customer>
     */

    @Override
    public List<Customer> readAllCustomersFromDatabase() {
        return loadAllCustomersFromDatabase();
    }

    /**
     * Calls createCustomerInDatabase(Customer customer)
     * @param customer
     */

    @Override
    public void createCustomerInDatabase(Customer customer) {
        createNewCustomerInDatabase(customer);
    }

    /**
     * Calls updateCustomerInDatabase(Customer customer)
     * @param customer
     */
    @Override
    public void updateCustomerInDatabase(Customer customer) {
        updateDataForCustomerInDatabase(customer);
    }

    /**
     * Calls deleteCustomerInDatabase(Customer customer)
     * @param customer
     */

    @Override
    public void deleteCustomerInDatabase(Customer customer) {
        deleteOldCustomerInDatabase(customer);
    }

    /**
     * Calls loadAllComicsFromDatabase()
     * @return List<Comic>
     */

    @Override
    public List<Comic> readAllComicsFromDatabase() {
        return loadAllComicsFromDatabase();
    }

    /**
     * Calls createComicInDatabase(Comic comic)
     * @param comic
     */
    @Override
    public void createComicInDatabase(Comic comic) {
        createNewComicInDatabase(comic);
    }

    /**
     * Calls deleteComicInDatabase(Comic comic)
     * @param comic
     */

    @Override
    public void deleteComicInDatabase(Comic comic) {
        deleteOldComicInDatabase(comic);
    }

    /**
     * Calls updateComicInDatabase(Comic comic)
     * @param comic
     */

    @Override
    public void updateComicInDatabase(Comic comic) {
        updateDataForComicInDatabase(comic);
    }

    /**
     * Calls readAllReservationsFromDatabase()
     * @return List<Reservation>
     */

    @Override
    public List<Reservation> readAllReservationsFromDatabase() {
        return loadAllReservationsFromDatabase();
    }

    /**
     * Calls readComicWhereID(Integer ID)
     * @param ID
     * @return Optional<Comic>
     */

    @Override
    public Optional<Comic> readComicWhereID(Integer ID) {
        return readComicID(ID);
    }

    /**
     * Calls readCustomerWhereID(Integer ID)
     * @param ID
     * @return Optional<Customer>
     */

    @Override
    public Optional<Customer> readCustomerWhereID(Integer ID) {
        return readCustomerID(ID);
    }

    /**
     * Calls createReservationInDatabase(Reservation reservation)
     * @param reservation
     */

    @Override
    public void createReservationInDatabase(Reservation reservation) {
        createNewReservationInDatabase(reservation);
    }

    /**
     * Calls deleteReservationInDatabase(Reservation reservation)
     * @param reservation
     */

    @Override
    public void deleteReservationInDatabase(Reservation reservation) {
        deleteOldReservationInDatabase(reservation);
    }

    /**
     * Calls updateReservationInDatabase(Reservation reservation)
     * @param reservation
     */

    @Override
    public void updateReservationInDatabase(Reservation reservation) {
        updateDataForReservationInDatabase(reservation);
    }

    /**
     * Checks if comic is connected to a reservation
     * @param comic
     * @return boolean
     */

    @Override
    public boolean comicConnectedToReservation(Comic comic) {
        List<Reservation> reservationList = loadAllReservationsFromDatabase();

        for (Reservation r : reservationList) {
            if (r.getComic().getComicID().equals(comic.getComicID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if customer is connected to a reservation
     * @param customer
     * @return boolean
     */

    @Override
    public boolean customerConnectedToReservation(Customer customer) {
        List<Reservation> reservationList = loadAllReservationsFromDatabase();

        for (Reservation r : reservationList) {
            if (r.getCustomer().customerID().equals(customer.customerID())) {
                System.out.println("Isti customer ID!");
                return true;
            }
        }
        return false;
    }

    /**
     * Closes the H2 database
     * @throws IOException
     */

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException nothing) {
            logger.error("Problem with SQL or connection...");
        }
    }

    /**
     * Makes a map consisting of key as an Integer representing unique COMIC_ID and value in the form of Integer that represents number of that comic reserved
     * @return Map of Integers
     */
    @Override
    public Map<Integer, Integer> getNumberOfComics() {
        List<Reservation> reservationList = readAllReservationsFromDatabase();
        Map<Integer, Integer> comicNumberMap = new HashMap<>();
        Integer numberOfComics;


        for (int i = 0; i < reservationList.size(); i++) {
            numberOfComics = 1;
            if (comicNumberMap.containsKey(reservationList.get(i).getComic().getComicID())) {
                numberOfComics = comicNumberMap.get(reservationList.get(i).getComic().getComicID());
                ++numberOfComics;
            }
            comicNumberMap.put(reservationList.get(i).getComic().getComicID(), numberOfComics);
        }
        return comicNumberMap;

    }
}



