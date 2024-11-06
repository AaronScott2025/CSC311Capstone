package com.example.csc311capstone.Functions;

/**
 * Budgeting:
 * By taking parameters of user salary, location, household size, estimated taxes(calculated), etc.,
 * the application will generate tables of month to month, or week to week budgeting strategies that adapt
 * to the user’s data.
 */

public class Budgeting {

    private int salary;
    private String Location;

    public Budgeting(int salary, String location) {
        this.salary = salary;
        Location = location;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public double investLimit(int percentage){
        return salary * percentage;

    }

    public double groceryLimit(int percentage){
        return salary * percentage;

    }

    public double gasLimit(int percentage){
        return salary * percentage;
    }

    public double Extras(int percentage){
        return salary * percentage;
    }
}
