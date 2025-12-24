package app;

import utils.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            Menu appMenu = new Menu();
            appMenu.start();
        } catch (Exception e) {
            System.out.println("FATAL ERROR: The application crashed unexpectedly.");
            e.printStackTrace();
        }
    }
}