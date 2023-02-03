package thread;

import entity.Change;
import javafx.scene.control.Alert;
import main.HelloApplication;

import java.util.List;

public class LastChangeThread implements Runnable{
    @Override
    public void run() {
        List<Change> list = HelloApplication.getDataSource().loadAllChanges();
        Change lastChange = list.get(list.size() - 1);

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Last change");
        alert.setHeaderText("This is the last change you have made!");
        alert.setContentText(lastChange.toString());

        alert.show();
    }
}
