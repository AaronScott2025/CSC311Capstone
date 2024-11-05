package com.example.csc311capstone.Functions;

/**
 * Budgeting:
 * By taking parameters of user salary, location, household size, estimated taxes(calculated), etc.,
 * the application will generate tables of month to month, or week to week budgeting strategies that adapt
 * to the user’s data.
 */

public class Budgeting {

    private int salary;
    private int householdSize;
    private String Location;

    public Budgeting(int salary, int householdSize, String location) {
        this.salary = salary;
        this.householdSize = householdSize;
        Location = location;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getHouseholdSize() {
        return householdSize;
    }

    public void setHouseholdSize(int householdSize) {
        this.householdSize = householdSize;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public int investLimit(){


    }

    public int groceryLimit(){
    //12.8 in ny

    }

    public int gasLimit(){

    }

    public int Extras(){

    }
}
