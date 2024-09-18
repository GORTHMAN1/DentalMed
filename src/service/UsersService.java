package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.User;

public class UsersService {

    private static final String USERS_FILE_PATH = "D:/proiectJavaFisiere/login.txt";

    public static Map<Integer, User> getUsers() {
        Map<Integer, User> userMap = new HashMap<>();

        try (Scanner fileScanner = new Scanner(new File(USERS_FILE_PATH))) {
            int id = 1;
            while (fileScanner.hasNextLine()) {
                String username = fileScanner.nextLine().trim();
                String password = fileScanner.nextLine().trim();

                User user = new User(username, password);
                userMap.put(id++, user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading user information from file: " + e.getMessage());
        }

        return userMap;
    }
}
