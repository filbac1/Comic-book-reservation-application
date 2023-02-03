package main;

import data.DataSource;
import data.DatabaseAndFileDataSource;
import exception.DataSourceException;
import exception.NoSuchDBPropertiesFile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.ComicQuantityReservedThread;
import thread.LastChangeThread;
import thread.NumberOfComicsReservedThread;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    public static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    private static Stage appStage;
    private static DataSource dataSource;
    private static final String DB_PROPERTIES_PATH = "/db.properties";

    @Override
    public void start(Stage stage) throws IOException {
        appStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("SNK - Reservations");
        stage.getIcons().add(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk568zyCAqXIBVMh97rz1n76eH1IH5K6etYg&usqp=CAU"));
        stage.setScene(scene);
        stage.show();

        var randomComicInfo = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> Platform.runLater(new ComicQuantityReservedThread(stage)))
        );

        randomComicInfo.setCycleCount(Timeline.INDEFINITE);
        randomComicInfo.play();

        var randomComicAlert = new Timeline(
                new KeyFrame(Duration.seconds(90), e -> Platform.runLater(new NumberOfComicsReservedThread()))
        );

        randomComicAlert.setCycleCount(Timeline.INDEFINITE);
        randomComicAlert.play();

        var lastChangeThread = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> Platform.runLater(new LastChangeThread()))
        );

        lastChangeThread.setCycleCount(Timeline.INDEFINITE);
        lastChangeThread.play();
    }

    public static void main(String[] args) {
        try (var ds = new DatabaseAndFileDataSource()) {
            dataSource = ds;
            launch();
        } catch (DataSourceException e) {
            logger.error("Couldn't connect to database", e);
        } catch (IOException e) {
            logger.error("Couldn't find database properties file: " + DB_PROPERTIES_PATH, e);
        } catch (NoSuchDBPropertiesFile e) {
            logger.error("Problem with getting the Properties file!");
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Function that makes it easier to display FXML file throughout the whole application
     * @param resourcePath
     */
    public static void showWindow(String resourcePath) {
        try {
            var window = (Parent) FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(resourcePath)));
            appStage.setScene(new Scene(window));
            appStage.show();
        } catch (IOException e) {
            logger.error("Problem with .fxml file reading!");
            e.printStackTrace();
        }
    }

    public static Stage getAppStage() {
        return appStage;
    }

    public static Logger getLogger() {
        return logger;
    }
}