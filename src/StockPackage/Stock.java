package StockPackage;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stock {
    private String name = "";//ticker symbol
    private String compName = "";//company name
    private double price = 0;//starting price
    private double dividCent=0;//dividend
    private DecimalFormat df = new DecimalFormat("0.00");//for formatting return and other money related things
    private ArrayList<Double> histPrices = new ArrayList<>();//shows the history of prices so far through the game
    private JsonObject stockHist;//shows the stock history as provided by the api
    private int sizeTable;//shows the size of stockHist for being used as an index later

    //precondition:String n for ticker for stock, String cn for company's name, double p for the starting price, double div for dividend, JsonObject r for the history of the stock through api, int size for the jsonobject as array's size
    //postcondition: constructor so sets instances
    public Stock(String n, String cn, double p, double div, JsonObject r, int size) {
        name = n;
        compName = cn;
        price = p;
        dividCent = div;
        stockHist = r;
        sizeTable = size;
    }

    //precondition:none
    //postcondition: returns current price
    public double getPrice() {
        return price;
    }

    //precondition: double change for changing price
    //postcondition: changes price of the stock
    public void changePrice(double change) {
        price+=change;
    }

    //precondition:int index representing day for getting something from the json object
    //postcondition:returns the json object turned to json array and finds the open price at index which is day
    public double getResp(int index) {
        return stockHist.get("dataset").getAsJsonObject().get("data").getAsJsonArray().get(index).getAsJsonArray().get(1).getAsDouble();
    }

    //precondition:none
    //postcondition:returns size of json object
    public int getSize() {
        return sizeTable;
    }

    //precondition:none
    //postcondition: returns the name of the stock
    public String getName() {
        return name;
    }

    //precondition:none
    //postcondition: returns the name of the company of the stock
    public String getCompName() {
        return compName;
    }

    //precondition:none
    //postcondition: returns dividend amount
    public double getDividCent() {
        return dividCent;
    }

    //precondition:double pr for adding price to list of past prices
    //postcondition: adds pr to histprices
    public void addHist(double pr) {
        histPrices.add(pr);
    }

    //precondition:none
    //postcondition: returns histprices
    public ArrayList<Double> getHistPrices() {
        return histPrices;
    }
}