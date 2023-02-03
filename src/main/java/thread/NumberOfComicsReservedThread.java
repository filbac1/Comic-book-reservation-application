package thread;

import data.DataSource;
import entity.Reservation;
import exception.MapDoesNotExistException;
import javafx.scene.control.Alert;
import main.HelloApplication;

import java.util.List;
import java.util.Map;

public class NumberOfComicsReservedThread implements Runnable{

    /**
     * Thread that alerts on how many copies of all comics should be set aside for reservation
     */
    @Override
    public void run() {
        DataSource dataSource = HelloApplication.getDataSource();
        List<Reservation> comicNumberList = null;

        comicNumberList = dataSource.readAllReservationsFromDatabase();

        Integer numberOfComicCopiesToBeReserved = comicNumberList.size();

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Number of comics you MUST have stored!");
        alert.setHeaderText("This is the overall number of all reserved comics!");
        alert.setContentText(String.valueOf(numberOfComicCopiesToBeReserved));

        alert.show();
    }
}
