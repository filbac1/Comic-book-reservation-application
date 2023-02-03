package sorter;

import entity.Comic;

import java.util.Comparator;

public class ComicSorter implements Comparator<Comic> {
    @Override
    public int compare(Comic c1, Comic c2) {
        int comp = c1.getBookName().compareTo(c2.getBookName());

        if (comp == 0) {
            comp = c1.getComicID().compareTo(c2.getComicID());
        }
        return comp;
    }
}
