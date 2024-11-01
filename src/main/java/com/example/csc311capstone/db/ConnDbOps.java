/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.csc311capstone.db;

import com.example.csc311capstone.Functions.User;

import java.sql.*;


public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://scota311server.mysql.database.azure.com/";
    final String DB_URL = "jdbc:mysql://scota311server.mysql.database.azure.com/311db";
    final String USERNAME = "scotadmin";
    final String PASSWORD = "Farmingdale14@";
    
    public  boolean connectToDatabase() {
        boolean hasRegistredUsers = false;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement  = conn.createStatement();
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

    public void registerUser(User s) {


    }
}
