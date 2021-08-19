package com.mobdeve.s15.group.nine.bookbay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static ArrayList<Notification> populateNotifications() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        ArrayList<Notification> notifications = new ArrayList<>();
        String[] titles = {"Girl, Woman, Other", "Shoko’s Smile", "The War for Kindness: Building Empathy in a Fractured World", "The Death of Vivek Oji", "The Little Prince"};
        String[] sellers = {"Lea Salonga", "Namjoon Kim", "Korina Sanchez", "Justin Bieber", "Taylor Swift"};
        String[] status = {"Approved", "Approved", "Declined", "Approved", "Declined"};
        int[] pictures = {R.drawable.girl, R.drawable.shoko_smile, R.drawable.war, R.drawable.death, R.drawable.little_prince};

        int time = 3;

        for (int i=0; i<titles.length;i++){
            time += 2;
            calendar.set(Calendar.HOUR_OF_DAY, time);
            notifications.add(new Notification(
                    titles[i],
                    sellers[i],
                    status[i],
                    pictures[i],
                    calendar));
        }
        return notifications;
    }
}
