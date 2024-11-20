package com.example.csc311capstone.Functions;

import java.io.FileNotFoundException;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;




/**
 * Based on a series of prompted questions, including information about your salary, necessities,
 * ideal lifestyle, habits, etc., will show you locations that will better suit your needs. Typically,
 * people are afraid of change because they are afraid of what’s unknown. This function lets the unknown
 * be a little more exposed, so that a user can plan accordingly.
 *
 * author: @AaronScott2025
 */

public class Locations implements Comparable<Locations>{
    private String state;
    private double COL;
    private double rec;
    private double crime;
    private double dist;

    public Locations(String state, double COL, double rec, double crime, double dist) {
        this.state = state;
        this.COL = COL;
        this.rec = rec;
        this.crime = crime;
        this.dist = dist;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getCOL() {
        return COL;
    }

    public void setCOL(double COL) {
        this.COL = COL;
    }

    public double getRec() {
        return rec;
    }

    public void setRec(double rec) {
        this.rec = rec;
    }

    public double getCrime() {
        return crime;
    }

    public void setCrime(double crime) {
        this.crime = crime;
    }

    /**
     * compareTo()
     * An override for the .sort() command. This allows for the states to be sorted by distance as calculated in the knn() method
     * (SEE BELOW FOR KNN()).
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Locations o) {
        return Double.compare(this.dist, o.dist); //Compare distance to distance
    }

    /**
     * knn(double,double,double)
     *
     * This method sets up the '3d space' (XYZ) where the plotted points in the space represent the 50 states, with variables
     * corresponding to their attributes. These attributes were calculated through research, and proportionately adjusted to fit the scope
     * of KNN. These weights are then compared to the placement of the users 'data point' (See MainController), through the
     * use of "Euclidean distance", which is used to calculate the distance between 2 data points. This is ran for every state, and
     * the result is passed to Main Controller for sorting/scoring.
     *
     * @param co
     * @param re
     * @param cri
     * @return
     * @throws FileNotFoundException
     */
    public List<Locations> knn(double co, double re, double cri) throws FileNotFoundException {
        List<Locations> StateData = new ArrayList<>(); //Store each locations data
        String csv = "src/main/resources/StateData.csv"; //Data file
        try(CSVReader r = new CSVReader(new FileReader(csv))) {
            List<String[]> rows = r.readAll(); //Arraylist filled with every state + variables
            for(int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                String state = row[0]; //State
                double COL = Double.parseDouble(row[1]); //Cost of Living
                double rec = Double.parseDouble(row[2]); //Recreation
                double crime = Double.parseDouble(row[3]); //Crimerate
                double dist = Math.sqrt(Math.pow(COL-co, 2)+Math.pow(rec-re, 2)+Math.pow(crime-cri, 2)); //Distance (Euclidian Distance)

                Locations sd = new Locations(state,COL,rec,crime,dist);
                StateData.add(sd);//add to state list
            }
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e); //Cant load file
        }
        return StateData; //return list
    }

}
