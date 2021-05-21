package StockPackage;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Person {
    private ArrayList<MyStocks> portfolio = new ArrayList<>();// name, and number of shares, price bought at. Shows the stocks that a person owns
    private ArrayList<Double> histMoney=new ArrayList<>();//history of the person's money as the game goes on
    private String name = "";//name
    private double money = 0;//starting money
    private int startDay = 0;//starting day of the user (not in date format)
    private DecimalFormat df = new DecimalFormat("#.00");//for formatting money related stuff

    //precondition: String for the name of the person, double m for starting money, int for knowing what day the user started at
    //postcondition: sets the instances
    public Person(String n, double m, int dayStart) {
        name = n;
        money = m;
        histMoney.add(m);
        startDay = dayStart;
    }

    //precondition:Stock of the object class in order to get price and check how much they can buy
    //postcondition: returns how many of those stock person can buy
    public void addStock(Stock stck, double amt) {
        DecimalFormat df = new DecimalFormat("#.0");
        boolean own = false;
        for (int i =0; i<portfolio.size();i++) {//checks the portfolio to see if the stock is already owned
            if (stck.getName().equals(portfolio.get(i).getName())) {
                own=true;
                portfolio.get(i).incrShares(amt);
            }
        }
        if (!own) {//if the stock is not owned then it will add a new one
            portfolio.add(new MyStocks(stck.getName(), amt, stck.getPrice()));
        }
    }

    //precondition:none
    //postcondition: returns the day the person started at
    public int getStartDay() {return startDay;}

    //precondition: none
    //postcondition: returns the history of person's money
    public ArrayList<Double> getHistMoney() { return histMoney; }

    //precondition:none
    //postcondition:returns the portfolio of ther user
    public ArrayList<MyStocks> getPortfolio() {
        return portfolio;
    }

    //precondition: none
    //postcondition: returns user name
    public String getName() {
        return name;
    }

    //precondition: none
    //postcondition: returns user's money
    public double getMoney() {
        return money;
    }

    //precondition: double amt to add to money
    //postcondition: adds amt to the overall balance
    public void moneyChange(double amt) {
        money+=Double.parseDouble(df.format(amt));
    }
}