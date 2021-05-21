package StockPackage;

public class MyStocks {
    private String name = "";//name of stock for comparing later in stockGUI
    private double amtShares =0;//amount of shares owned by this person
    private double priceBought;//the price the stock was bought at for later use

    //precondition:String n for name, amt for amount of shares owned, and pB for priceBought at by the user
    //postcondition: just declares important variables
    public MyStocks(String n, double amt, double pB) {
        name = n;
        amtShares = amt;
        priceBought = pB;
    }

    //precondition:amt to know how much you want to add to the amtShares
    //postcondition:adds amt to amtShares
    public void incrShares(double amt) {
        amtShares+=amt;
    }

    //precondition: none
    //postcondition: returns name
    public String getName() {
        return name;
    }

    //precondition: none
    //postcondition: returns price the stock bought at
    public double getPriceBought() {return priceBought;}

    //precondition: none
    //postcondition: returns the amount of shares owned by user
    public double getAmtShares() {
        return amtShares;
    }
}