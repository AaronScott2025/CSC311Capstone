package com.example.csc311capstone.Functions;


/**
 * User:
 * The database will have the data for each user, as well as password, location, and salary
 *
 * author: @AaronScott2025
 */
public class User {
    private String username;
    private String password;
    private String location;
    private int salary;

    public User(int salary, String location, String password, String username) {
        this.salary = salary;
        this.location = location;
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
