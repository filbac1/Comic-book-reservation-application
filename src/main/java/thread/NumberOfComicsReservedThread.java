package thread;

import data.DataSource;
import exception.MapDoesNotExistException;
import javafx.scene.control.Alert;
import main.HelloApplication;

import java.util.Map;

public class NumberOfComicsReservedThread implements Runnable{
    @Override
    public void run() {
        DataSource dataSource = HelloApplication.getDataSource();
        Map<Integer, Integer> comicNumberMap = null;

        try {
            comicNumberMap = dataSource.getNumberOfComics();
        } catch (MapDoesNotExistException e) {
            e.printStackTrace();
        }

        Integer numberOfComicCopiesToBeReserved = 0;

        for (Map.Entry<Integer,Integer> entry : comicNumberMap.entrySet()) {
            numberOfComicCopiesToBeReserved += entry.getValue();
        }

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Number of comics you MUST have stored!");
        alert.setHeaderText("This is the overall number of all reserved comics!");
        alert.setContentText(String.valueOf(numberOfComicCopiesToBeReserved));

        alert.show();
    }
}
