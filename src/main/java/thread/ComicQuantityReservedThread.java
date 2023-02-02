package thread;

import data.DataSource;
import entity.Comic;
import entity.User;
import exception.DeletedElementException;
import exception.MapDoesNotExistException;
import javafx.stage.Stage;
import main.HelloApplication;

import java.util.*;

public class ComicQuantityReservedThread implements Runnable {
    private final Stage stage;

    public ComicQuantityReservedThread(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void run() {
        DataSource dataSource = HelloApplication.getDataSource();
        Map<Integer, Integer> comicNumberMap;
        List<Comic> allComics = HelloApplication.getDataSource().readAllComicsFromDatabase();

        List<Integer> keys = new ArrayList<>();

        try {
            comicNumberMap = dataSource.getNumberOfComics();
        } catch (MapDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<Integer,Integer> comic : comicNumberMap.entrySet()) {
            keys.add(comic.getKey());
        }

        Random rand = new Random();

        Comic randomKey = allComics.get(rand.nextInt(allComics.size()));
        int randomValue = 0;
        try {
            randomValue = comicNumberMap.get(randomKey.getComicID());
        } catch (DeletedElementException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.getMessage();
        }


        String title;

        if (randomValue > 1) {
            title = randomKey.getBookName() + " has to be reserved in at least " + randomValue + " copies!";
        } else {
            title = randomKey.getBookName() + " has to be reserved in at least " + randomValue + " copy!";
        }


        stage.setTitle(title);


    }
}
