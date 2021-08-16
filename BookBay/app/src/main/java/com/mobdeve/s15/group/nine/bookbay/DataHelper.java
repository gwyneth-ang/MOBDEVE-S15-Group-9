package com.mobdeve.s15.group.nine.bookbay;

import java.util.ArrayList;

public class DataHelper {

    public static ArrayList<Book> populateData() {
        ArrayList<Book> books = new ArrayList<>();

        String[] titles = {"Girl, Woman, Other", "Shoko’s Smile", "The War for Kindness: Building Empathy in a Fractured World", "The Death of Vivek Oji", "The Little Prince"};
        String[] authors = {"Bernadine Evaristo", "Choi Eunyoung", "Jamil Jaki", "Temp", "Antoine"};
        int[] pictures = {R.drawable.girl, R.drawable.shoko_smile, R.drawable.war, R.drawable.death, R.drawable.little_prince};

        for (int i=0; i<titles.length;i++){
            books.add(new Book(
                    titles[i],
                    authors[i],
                    pictures[i]));
        }
        return books;
    }
}
