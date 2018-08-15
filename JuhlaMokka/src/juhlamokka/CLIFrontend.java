/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 *
 * @author s1800571
 */
public class CLIFrontend<T> {
    public void start() {
        Menu m = new Menu("Title", "desc", Arrays.asList("1. Add", "2. Update", "3. Remove", "4. Exit"));
    }
}

class Menu {
    private String title;
    private String description;
    private List<String> list;
    
    Menu(String title, String description, List<String> list) {
        this.title = title;
        this.description = description;
        this.list = list;
        
        display();
    }
    
    private void display() {
        System.out.println("=" + title + "=");
        System.out.println(description);
        
        for(String item : list) {
            System.out.println(item);
        }
    }
}