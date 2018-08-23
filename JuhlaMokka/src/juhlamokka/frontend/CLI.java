package juhlamokka.frontend;

import java.util.Arrays;
import java.util.List;

/**
 * A command line frontend for the Juhlamokka inventory management software
 * @param <T>
 */
public class CLI<T> {
    public void start() {
        Menu m = new Menu("Title", "desc", Arrays.asList("1. Add", "2. Update", "3. Remove", "4. Exit"));
    }
}

class Menu {
    private final String title;
    private final String description;
    private final List<String> list;
    
    Menu(String title, String description, List<String> list) {
        this.title = title;
        this.description = description;
        this.list = list;
        
        display();
    }
    
    private void display() {
        System.out.println("=" + title + "=");
        System.out.println(description);
        
        list.forEach((item) -> {
            System.out.println(item);
        });
    }
}