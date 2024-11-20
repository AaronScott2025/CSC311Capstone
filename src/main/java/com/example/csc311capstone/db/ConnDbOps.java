/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.csc311capstone.db;

import com.example.csc311capstone.Functions.User;

import java.sql.*;

/**
 * ConnDbOps-
 * All database operations necessary for this project can be found here.
 *
 * author: @AaronScott2025
 */
public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://scota311server.mysql.database.azure.com/";
    final String DB_URL = "jdbc:mysql://scota311server.mysql.database.azure.com/311db";
    final String USERNAME = "scotadmin";
    final String PASSWORD = "Farmingdale14@";

    /**
     * connectToDatabase()
     * Tries to establish a connection with the database. If its found to have users, then it returns true, otherwise it
     * will return false.
     * @return
     */
    public boolean connectToDatabase() {
        boolean hasRegistredUsers = false;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(hasRegistredUsers);
        return hasRegistredUsers;
    }

    /**
     * registerUser(User)
     * Takes in a user, and initializes them in the database with an encrypted password (SEE LOGIN.JAVA), and their username.
     * Everything else is null for now, and is set in the profile editor in the main GUI (MAINCONTROLLER).
     * @param s
     */
    public void registerUser(User s) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO users (username, password, location, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, s.getUsername());
            preparedStatement.setString(2, s.getPassword());
            preparedStatement.setString(3, s.getLocation());
            preparedStatement.setString(4, Integer.toString(s.getSalary()));

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("User registered successfully");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * getUser(String)
     * Uses the primary key (username) to retireve the user, initialize the user to a User dataclass, and return it to the
     * function that called it (LOGINCONTROLLER).
     * @param username
     * @return
     */
    public User getUser(String username) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User s = new User(resultSet.getInt("salary"), resultSet.getString("location"), resultSet.getString("password"), resultSet.getString("username"));
                return s;
            } else {
                User s = new User(0, "", "", "");
                return s;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * updateUser(user)
     * Updates the users info in the database.
     * @param u
     * @return
     */
    public boolean updateUser(User u) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "UPDATE users SET salary = ?, location = ? WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, u.getSalary());
            preparedStatement.setString(2, u.getLocation());
            preparedStatement.setString(3, u.getUsername());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("User updated successfully");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
