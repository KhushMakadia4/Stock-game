package StockPackage;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class News {
    private String  news;//the string made in the news functions and returned later
    private String[] medDec = new String[] {"goals", "recall", "social", "strike", "exposure"};//ideas of news for medium decrease in stock percent change
    private String[] highDec = new String[] {"ceo", "lawsuit"};//ideas for high decrease
    private String[] medInc = new String[] {"release", "charity", "branch", "social", "marketing"};//ideas for mediocre increase
    private String[] highInc = new String[] {"forbes", "ceo", "competitor"};//ideas for high increase
    private String[] marketUp = new String[] {"trade", "tax", "lowUnemp"};//ideas for whole market up
    private String[] marketDown = new String[] {"trade", "tax", "overflow", "investCap"};//ideas for whole market down;
    private DecimalFormat df = new DecimalFormat("#.00");//for formatting numbers

    //precondition:double percentChange to be examined, Stock x to get info like names, date to show date in the return string
    //postcondition: returns carefully constructed string of news to the controller
    public String makeNews(double percentChange, Stock x, LocalDate dateN) {
        news = "";
        news+=dateN + " ";
        String temp;
        //tons of if statements and then putting in pieces of string in the array as a smart way to do stuff.
        if (percentChange<0) {//decrease
            if (percentChange<=0 &&percentChange>-2) {//not significant
                return "";
            } else if (percentChange<=-2 && percentChange>-7) {//medium significant
                temp = medDec[(int)(Math.random()*medDec.length)];
                switch(temp) {
                    case "goals":
                        news += x.getCompName() + " could not meet board goals for company in the fiscal quarter.   ";
                        break;
                    case "recall":
                        news += "A product from " + x.getCompName() + " has been recalled for testing.   ";
                        break;
                    case "social":
                        news += x.getCompName() + " posted an offensive tweet.   ";
                        break;
                    case "strike":
                        news += "Employess of " + x.getCompName() + " have undergone strike.   ";
                        break;
                    default:
                        news += "Leaked information of " + x.getCompName() + " are released on the internet.   ";
                }
            } else {//very high
                temp = highDec[(int)(Math.random()*highDec.length)];
                switch (temp) {
                    case "ceo":
                        news += x.getCompName() + "'s CEO found dead on coding street.   ";
                        break;
                    case "lawsuit":
                        news+= x.getCompName() + " will be dragged to court by the cpsc and ul and many of its employees.   ";
                        break;
                }
            }
            return news + x.getName() + " down " + df.format(percentChange*-1) + "%";
        } else {//increase
            if (percentChange>=0 && percentChange<2) {//not significant
                return "";
            } else if (percentChange>=2 && percentChange<7){//medium significant
                temp = medInc[(int)(Math.random()*medInc.length)];
                switch (temp) {
                    case "release":
                        news+= x.getCompName() + " will be releasing a new product tomorrow.   ";
                        break;
                    case "charity":
                        news+= x.getCompName() + " has donated millions to a worldwide charity.   ";
                        break;
                    case "branch":
                        news += "A new branch in code village will be opened by " + x.getCompName() + "   ";
                        break;
                    case "social":
                        news += "A viral encouraging tweet was posted by " + x.getCompName() + "   ";
                        break;
                    default:
                        news += x.getCompName() + " launches a jumbo marketing campaign for its products.   ";
                }
            } else {//very high //"forbes", "ceo", "competitor"
                temp = highInc[(int)(Math.random()*highInc.length)];
                switch (temp) {
                    case "forbes":
                        news += "Many business websites including forbes and businessinsider gave " + x.getCompName() + " a perfect review.   ";
                        break;
                    case "ceo":
                        news += "A plan devised by the CEO of " + x.getCompName() + "has worked boosted company profits";
                        break;
                    case "competitor":
                        news+= x.getCompName() + " just destroyed another competitor of its and forced them to go bankrupt.   ";
                        break;
                }
            }
            return news + x.getName() + " up " + df.format(percentChange) + "%";
        }
    }

    //precondition: double change because of examination of sales, stock for names etc, date for display
    //postcondition: returns a nice string that will show monthly report of x month for this stock
    public String intervNews(double percentChange, Stock x, LocalDate date) {
        news = "";
        if (percentChange>0) {//good month
            news = x.getCompName() + " had a good month for sales and operations for " + date.getMonth().toString().toLowerCase();
        } else {//bad month
            news = x.getCompName() + " had a bad month for sales and operation for " + date.getMonth().toString().toLowerCase();
        }
        return news;
    }

    //precondition:localdate so that string can have it
    //postcondition:it has to return both the change in stock pricing from now and the actual news
    public Object[] stockMarkNews(LocalDate date) {
        Object[] retArr = new Object[2];
        String news =date.toString() + ": ";
        if ((int)(Math.random()*2)==0) {//up
            retArr[0] = (Math.random()*4) + 1;
            String temp = marketUp[(int)(Math.random()*marketUp.length)];
            switch (temp) {//goes through good ones
                case "trade":
                    news = "U.S. opens trades with " + ((int)(Math.random()*10)+2) + " other countries. Stock market goes up";
                    break;
                case "tax":
                    news = "Interest and business taxes are reduced by the government";
                    break;
                default:
                    news = ((int)(Math.random()*15)+5) + "% decrease in unemployment. Hopes up for the market";
            }
        } else {//down
            retArr[0] = ((Math.random()*4) + 1)*-1;
            String temp = marketDown[(int)(Math.random()*marketDown.length)];
            switch (temp) {//goes through bad situations
                case "trade":
                    news = "U.S. closes trades with " + ((int)(Math.random()*10)+2) + " other countries. Trade wars continue";
                    break;
                case "tax":
                    news = "The government increase federal tax and interest rates. Market in danger";
                    break;
                case "overflow":
                    news = "A boom in the market eventually leads to a decrease and leaving many in a loss.";
                    break;
                default:
                    news = "A new formality makes investors have a cap on buying set by the U.S. government";
            }
        }
        retArr[1] = news;
        return retArr;
    }

}
