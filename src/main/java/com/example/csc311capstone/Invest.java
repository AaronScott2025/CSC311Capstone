package com.example.csc311capstone;

/**
 * Invest:
 * Features an investment calculator, which will allow users to take how much money they currently have,
 * and how much they plan to invest every month on top of that, to show how much money they would have in ‘x’ amount
 * of years through different investment methods, such as the S&P500, money market, bonds, etc., in addition to showing the
 * pros and cons of doing each, and encouraging a diverse portfolio and additionally showing what that can look like.
 */

public class Invest {
    private int initial;
    private int years;
    private int yearlyInvestment;

    public Invest(int initial, int years, int yearlyInvestment) {
        this.initial = initial;
        this.years = years;
        this.yearlyInvestment = yearlyInvestment;
    }

    public int getInitial() {
        return initial;
    }

    public void setInitial(int initial) {
        this.initial = initial;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getYearlyInvestment() {
        return yearlyInvestment;
    }

    public void setYearlyInvestment(int yearlyInvestment) {
        this.yearlyInvestment = yearlyInvestment;
    }

    public double[] eightPercent() { //S&P500
        double[] compound = new double[years];
        double temp = initial;
        for(int i = 1; i <= years; i++) {
            temp = yearlyInvestment + (temp*1.08);
            compound[i] = temp;
        }
        return compound;
    }
    public double[] fivePercent() { //High yield savings account
        double[] compound = new double[years];
        double temp = initial;
        for(int i = 1; i <= years; i++) {
            temp = yearlyInvestment + (temp*1.05);
            compound[i] = temp;
        }
        return compound;
    }
    public double[] threePercent() { //Bonds
        double[] compound = new double[years];
        double temp = initial;
        for(int i = 1; i <= years; i++) {
            temp = yearlyInvestment + (temp*1.03);
            compound[i] = temp;
        }
        return compound;
    }

}
