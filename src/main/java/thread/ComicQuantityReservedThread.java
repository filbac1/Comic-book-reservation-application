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

        try {
            comicNumberMap = dataSource.getNumberOfComics();
        } catch (MapDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        Random rand = new Random();
        List<Integer> comicIDList = new ArrayList<>(comicNumberMap.keySet());
        System.out.println(comicIDList);

        Integer randComicID = comicIDList.get(rand.nextInt(comicIDList.size()));
        System.out.println(randComicID);
        Comic randComic = null;

        for (Comic c : allComics) {
            if(c.getComicID().equals(randComicID)) {
                randComic = c;
                break;
            }
        }

        System.out.println(randComic);

        int randomValue = 0;
        try {
            randomValue = comicNumberMap.get(randComicID);
        } catch (DeletedElementException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.getMessage();
        }


        String title;

        if (randomValue != 1) {
            title = randComic.getBookName() + " has to be reserved in at least " + randomValue + " copies!";
        } else {
            title = randComic.getBookName() + " has to be reserved in at least " + randomValue + " copy!";
        }

        stage.setTitle(title);

    }
}
