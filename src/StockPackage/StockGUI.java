package StockPackage;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class StockGUI implements Initializable {
    //fxml instances
    @FXML private ListView playersLst;//listview to show players
    @FXML private ListView allStocksLst;//listview to show market stocks
    @FXML private ListView playerStocksLst;//show shares of stocks of player
    @FXML private ListView newsListView;//news shown by intervals
    @FXML private TextField buyTxtField;//used to get the shares of stocks bought
    @FXML private TextField sellTxtField;//used to get the shares of stocks sold
    @FXML private TextField nameTF;//gets the name of person                                
    @FXML private TextField startBalTF;//starting balance when started                      
    @FXML private LineChart moneyChart;//shows chart of player money
    @FXML private LineChart buystockChart;//shows chart of stock in buy screen
    @FXML private LineChart sellStockCht;//shows chart of stock in sell screen
    @FXML private AnchorPane addplayer;//anchorpanes acting as screens
    @FXML private AnchorPane buyScreen;
    @FXML private AnchorPane sellScreen;
    @FXML private AnchorPane newsScreen;
    @FXML private Label dateLbl;//shows date
    @FXML private Label playerNameLbl;//shows player's name
    @FXML private Label playerMoneyLbl;//shows player's money
    @FXML private Label moneyLbl;//shows player's money on top of screen
    @FXML private Label stockTickLbl;//shows ticker symbol
    @FXML private Label buyCompLbl;//shows name of company
    @FXML private Label buySMoneyLbl;//money of stock
    @FXML private Label sellTickLbl;//shows ticker symbol in sell screen
    @FXML private Label sellCompLbl;//shows company name symbol in sell screen
    @FXML private Label sellPriceatLbl;//shows price at symbol in sell screen
    @FXML private Label sellBoughtAtLbl;//shows price that the stock was bought at in the sell screen
    @FXML private Label sellSharesLbl;//shows shares of stock owned
    @FXML private Label buyDividLbl;//dividends
    @FXML private Label sellDividLbl;//dividends
    @FXML private TextArea instrucTxtArea;//instructions provided here
    @FXML private Button cancelBtn;//button in addplayer screen that will be used for not adding the player (exit button)
    @FXML private Button advTime;//used at the end if game ends. Disabled if true
    @FXML private ToolBar toolBar;//used at the end if game ends. Disabled if true


    //PLEASE READ: MY UI WAS INSPIRED BY MARKETWATCH.COM SO THAT CREDIT GOES WHERE ITS DUE


    //class instances
    ArrayList<Person> players = new ArrayList<>();//collection of players
    ArrayList<Stock> marketStocks = new ArrayList<>();//collection of all the stocks in the market
    News news = new News();//will help make news later  

    //primitive or other instances
    int playInd=0;//index of player location
    int day = 0;//used for localdates and linecharts shows days past
//    int markNewsMod = (int)((Math.random()*316) + 50);
    int markNewsMod = 3;
    double markNewsCent = 0.0;

    //functions

    //precondition:URL and ResourceBundle so that the function is initializable
    //postcondition:calls api and sets up the market and ui
    public void initialize(URL n, ResourceBundle rb) {
        System.out.println(markNewsMod);
        moneyChart.setCreateSymbols(false);
        buystockChart.setCreateSymbols(false);
        sellStockCht.setCreateSymbols(false);
        buyScreen.setVisible(false);
        sellScreen.setVisible(false);
        newsScreen.setVisible(false);
        cancelBtn.setVisible(false);
        updPlayers();
        //api under here
        try {
            Scanner temp = new Scanner(new FileReader("src/resources/StockInfo"));
            String[] str;
            Object[] x;
            System.out.println("Will take long to start as api is called multiple times");
            while (temp.hasNextLine()) {//while the stock textfile has stock tickers in there
                str = temp.nextLine().split(",");
                x= checkAPI(str[0]);
                if ((double)x[0]!=0.0) {//checks if the stock called by api was not provided by api
                    marketStocks.add(new Stock(str[0],str[1],(double)x[0], Double.parseDouble(df.format((Math.random()*5)+1)), (JsonObject)x[1], (int)x[2]));
                }
            }
        } catch (FileNotFoundException e) {
        }
        date = LocalDate.of(2013,9,1);
        dateLbl.setText(date.toString());
        for (int i =0;i<marketStocks.size();i++) {//updates the fxml part showing all market stocks with all of this
            allStocksLst.getItems().add(marketStocks.get(i).getName() + " with price: " + marketStocks.get(i).getPrice());
        }
    }

    //precondition:none
    //postcondition:will show only the portfolio screen aka home screen
    @FXML
    public void showPortScreen() {
        addplayer.setVisible(false);
        buyScreen.setVisible(false);
        sellScreen.setVisible(false);
        newsScreen.setVisible(false);
    }

    //precondition:none
    //postcondition:shows only buy screen
    @FXML
    public void showBuyScreen() {
        showPortScreen();
        buyScreen.setVisible(true);
    }

    //precondition:none
    //postcondition:shows only sell screen
    @FXML
    public void showSellScreen() {
        showPortScreen();
        sellScreen.setVisible(true);
    }

    //precondition: none
    //postcondition:shows the news screen
    @FXML
    public void showNewsScreen() {
        showPortScreen();
        newsScreen.setVisible(true);
    }

    //precondition: none
    //postcondition:shows the add player screen
    @FXML
    public void showAddPlayer() {
        showPortScreen();
        addplayer.setVisible(true);
    }

    //precondition: none
    //postcondition: removes the add player screen
    @FXML public void cancelAddPlayer() {
        addplayer.setVisible(false);
    }

    //precondition:none
    //postcondition:shows instructions to play the game in the text area
    @FXML public void showInstruc() {
        instrucTxtArea.setText("Instructions:\nAfter adding players there is a listview in the toolbar in the top of the screen where the players you add will appear. As you click on players" +
                "it will change some data and show you the specifications of that player and you can sell only that player's stocks and if they are not a bot. To change screens use the toolbar" +
                "up top and flip them. In order to advance the game press the next day button upstairs and then it will display the next stock. This game only runs to december 28 2017 and starts" +
                " at september 1 2013. Be sure to use the money wisely and not end up in trouble. The news shows news about how stocks go up or down between previous days. A dividend will return" +
                "amount every month that the player has had that stock.");
    }



    LocalDate date;//gets the date
    DecimalFormat df = new DecimalFormat("#.00");//to make everything look cent like
    URL url;//url to use for contacting internet
    HttpURLConnection connection;//sets up connection with internet and the program
    JsonParser parser;//parses the response from connection
    JsonObject myResponse;//turns the parsed info to a organized and easy to read object
    //precondition:ticker of the stock string to allow the api to target a specific stock
    //postcondition:gets the stock history and puts it in the stock class of that specific stock. Returns Object array full of different things like starting price,history of stock, size of history of stock
    public Object[] checkAPI(String symbol) {
        Object[] retArr = new Object[] {0.0, new JsonObject(), 0};
        try { //code from: https://www.youtube.com/watch?v=NyPp3dAxb0I
            //more code from: https://stackoverflow.com/questions/22461663/convert-inputstream-to-jsonobject/25136732
            //api HttpURLConnection notes: https://www.codota.com/code/java/methods/java.net.HttpURLConnection/setRequestProperty
            //api documentation: https://www.worldtradingdata.com/documentation#full-history //old api
            //api key and link documentation: https://www.worldtradingdata.com/home // old api
            //JSON reading response and testing program: postman software downloaded from google //really nice i recommend it to help students read json responses from apis
            url = new URL("https://www.quandl.com/api/v3/datasets/EOD/"+symbol+".json?api_key=-4_M3xUCWskP2pbd-g3D");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            parser = new JsonParser();
            myResponse = (JsonObject) parser.parse(new InputStreamReader(connection.getInputStream()));
            if ((myResponse.get("dataset").getAsJsonObject().get("start_date").getAsString().equals("2013-09-01"))) {//checks if the starting data of the stock history is 2013-09-01
                retArr[2] = myResponse.getAsJsonObject("dataset").getAsJsonArray("data").size()-1;
                retArr[0] = myResponse.getAsJsonObject("dataset").getAsJsonArray("data").get((int)retArr[2]).getAsJsonArray().get(1).getAsDouble();
                retArr[1] = myResponse;
            }
            System.out.println(myResponse.get("dataset").getAsJsonObject().get("data").getAsJsonArray().get(1).getAsDouble());
        }catch (Exception e) {
        }
        return retArr;
    }

    //precondition:none
    //postcondition: updates the players' infos with their names and money
    public void updPlayers() {
        playersLst.getItems().clear();
        for (int i =0;i<players.size();i++) {//goes through the player list
            playersLst.getItems().add(players.get(i).getName() + " , Money: " + df.format(players.get(i).getMoney()));
        }
    }

    //precondition:none
    //postcondition: makes new person in the game
    @FXML
    public void addPerson() {
            try {
                if (Double.parseDouble(startBalTF.getText())<0) {//unbreakable ui aspect
                    JOptionPane.showMessageDialog(null, "Please do not write a negative number in the money field");
                }else {
                    players.add(new Person(nameTF.getText(), Double.parseDouble(startBalTF.getText()), day));
                    updPlayers();
                    addplayer.setVisible(false);
                    cancelBtn.setVisible(true);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Fill out valid answers in the textfield(s)");
            }
    }

    //precondition:none
    //postcondition:shows the stocks that a person owns and also shows the linechart with user money and sets up user info
    @FXML
    public void dispShares() {
        //index
        try {
            playerStocksLst.getItems().clear();
            playInd = playersLst.getSelectionModel().getSelectedIndex();
            //moneyChart
            moneyChart.getData().clear();
            XYChart.Series series = new XYChart.Series();
            series.setName(players.get(playInd).getName() + "'s Money");
            for (int i = 0; i < players.get(playInd).getHistMoney().size(); i++) {//goes through player's history of money for linechart
                series.getData().add(new XYChart.Data(i+players.get(playInd).getStartDay(), players.get(playInd).getHistMoney().get(i)));
            }
            moneyChart.getData().add(series);
            playerNameLbl.setText(players.get(playInd).getName());
            playerMoneyLbl.setText(df.format(players.get(playInd).getMoney()));
            moneyLbl.setText(df.format(players.get(playInd).getMoney()));
            //disp stocks of player
            for (int i = 0; i < players.get(playInd).getPortfolio().size(); i++) {//goes through the stocks and shares that person owns so that it can update the listview
                playerStocksLst.getItems().add(players.get(playInd).getPortfolio().get(i).getAmtShares() + " of " + players.get(playInd).getPortfolio().get(i).getName());
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select a player from the players list");
        }
    }

    //precondition:none
    //postcondition: sells the stock from a person's portfolio and shows it on news
    @FXML
    public void sellStck() {
        try {
            try {
                playInd = playersLst.getSelectionModel().getSelectedIndex();
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please select a player from the listview");
            }
            int stockInd = playerStocksLst.getSelectionModel().getSelectedIndex();
            double shares = Double.parseDouble(sellTxtField.getText());
            double tempsh = players.get(playInd).getPortfolio().get(stockInd).getAmtShares();
            if (shares > 0) {//shares could be negative
                if (tempsh >= shares) {//if player's shares are more than selling textfield number
                    for (Stock x : marketStocks) {//goes through market stocks for checking price
                        if (x.getName().equals(players.get(playInd).getPortfolio().get(stockInd).getName())) {//checks the stock from the player's portfolio stock
                            players.get(playInd).moneyChange(shares * x.getPrice());
                            players.get(playInd).getPortfolio().get(stockInd).incrShares((-1 * shares));
                            if (tempsh == shares) {//will remove the stock
                                players.get(playInd).getPortfolio().remove(stockInd);
                            }
                            newsListView.getItems().add(players.get(playInd).getName() + " sold " + shares + " shares of " + x.getName() + " on " + date.plusDays(day));
                            break;
                        }
                    }
                    updPlayers();
                } else {
                    JOptionPane.showMessageDialog(null, "YOU DON'T HAVE THAT MANY SHARES");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Do not put negative numbers in the shares textfield");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select something valid in the listviews");
        }
    }

    //precondition:none
    //postcondition: shows the info of a stock in the sell screen
    @FXML
    public void showSellCht() {
        int playInd = playersLst.getSelectionModel().getSelectedIndex();
        int index = playerStocksLst.getSelectionModel().getSelectedIndex();
        for (Stock x:marketStocks) {//goes through all stocks
            if (x.getName().equalsIgnoreCase(players.get(playInd).getPortfolio().get(index).getName())) {//finds the stock selected on the marketstocks list and displays the price
                sellStockCht.getData().clear();
                XYChart.Series series = new XYChart.Series();
                series.setName(x.getName() + "'s Chart");
                for (int i =0;i<day;i++) {//for linechart data
                    series.getData().add(new XYChart.Data(i,x.getHistPrices().get(i)));
                }
                sellTickLbl.setText(x.getName());
                sellCompLbl.setText(x.getCompName());
                sellPriceatLbl.setText(Double.toString(x.getPrice()));
                sellDividLbl.setText("Dividend: "+ x.getDividCent() + " per share");
                sellBoughtAtLbl.setText(Double.toString(players.get(playInd).getPortfolio().get(index).getPriceBought()));
                sellSharesLbl.setText(Double.toString(players.get(playInd).getPortfolio().get(index).getAmtShares()));
                sellStockCht.getData().add(series);
            }
        }
    }

    //precondition:none
    //postcondition: shows buy chart on buy screen
    @FXML
    public void showBuyCht() {
        try {
            int index = allStocksLst.getSelectionModel().getSelectedIndex();
            buystockChart.getData().clear();
            XYChart.Series series = new XYChart.Series();
            series.setName(marketStocks.get(index).getName() + "'s Chart");
            for (int i = 0; i < day; i++) {//adds data per day on the linechart data collection object
                series.getData().add(new XYChart.Data(i, marketStocks.get(index).getHistPrices().get(i)));
            }
            stockTickLbl.setText(marketStocks.get(index).getName());
            buyCompLbl.setText(marketStocks.get(index).getCompName());
            buyDividLbl.setText("Dividend: "+ marketStocks.get(index).getDividCent() + " per share");
            buySMoneyLbl.setText(Double.toString(marketStocks.get(index).getPrice()));
            buystockChart.getData().add(series);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Please select a stock in the listview");
        }
    }

    //precondition: none
    //postcondition: allows user to buy the stock called by button
    @FXML
    public void buyStock() {
        try {
            try {
                playInd = playersLst.getSelectionModel().getSelectedIndex();
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please select a player from the player listview");
            }
            int stockInd = allStocksLst.getSelectionModel().getSelectedIndex();
            double shares = Double.parseDouble(buyTxtField.getText());
            if (shares>0) {//shares could be negative which is bad ui
                if (shares * marketStocks.get(stockInd).getPrice() > players.get(playInd).getMoney()) {//checks if user has enough money
                    JOptionPane.showMessageDialog(null, "NOT ENOUGH MONEY");
                } else {//user has enough money
                    players.get(playInd).addStock(marketStocks.get(stockInd), shares);
                    players.get(playInd).moneyChange(-1 * (marketStocks.get(stockInd).getPrice() * shares));
                    newsListView.getItems().add(players.get(playInd).getName() + " bought " + shares + " shares of " + marketStocks.get(stockInd).getName() + " on " + date.plusDays(day));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Do not type negative numbers in the shares field");
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select a stock in the listview");
        }
        dispShares();
        updPlayers();
    }

    //precondition:none
    //postcondition: advances to the next day. Changes prices, adds news, updates players' money in their histories
    @FXML
    public void advTime() {
        allStocksLst.getItems().clear();
        day++;
        date = LocalDate.of(2013,9,1).plusDays(day);
        dateLbl.setText(date.toString());
        int[] newsInds = new int[(int)(Math.random()*5)];//i have it as an arraylist so that i can have news from different companies
        for (int i = 0;i<newsInds.length;i++) {//goes through how many companies are getting news of               //news that affect an individual stock
            newsInds[i] = (int)(Math.random()*marketStocks.size());
            for (int x:newsInds) {//these few lines make sure that all the indices are different
                if (x==newsInds[i]) {
                    while(x==newsInds[i]) {
                        newsInds[i] = (int) (Math.random()*marketStocks.size());
                    }
                }
            }
            //calculates and sends percent change
            double p = ((marketStocks.get(newsInds[i]).getResp(marketStocks.get(newsInds[i]).getSize()-day) - marketStocks.get(newsInds[i]).getPrice())/marketStocks.get(newsInds[i]).getPrice())*100;
            String n = news.makeNews(p,marketStocks.get(newsInds[i]),date.plusDays(day));
            if (!n.equals("")) {//checks if the percent change is very big
                newsListView.getItems().add(news.makeNews(p, marketStocks.get(newsInds[i]), date.plusDays(day)));
            }
        }

        if (day%30==0) {//monthly reports              //news at an interval
            newsInds = new int[(int)(Math.random()*2)];
            for (int i = 0;i<newsInds.length;i++) {//sames as above makes sure no repeats
                newsInds[i] = (int)(Math.random()*marketStocks.size());
                for (int x:newsInds) {
                    if (x==newsInds[i]) {
                        while(x==newsInds[i]) {
                            newsInds[i] = (int) (Math.random()*marketStocks.size());
                        }
                    }
                }
                double p = ((marketStocks.get(newsInds[i]).getResp(marketStocks.get(newsInds[i]).getSize()-day) - marketStocks.get(newsInds[i]).getPrice())/marketStocks.get(newsInds[i]).getPrice())*100;
                newsListView.getItems().add(news.intervNews(p,marketStocks.get(newsInds[i]),date.plusDays(day)));
            }
        }

        if (day==markNewsMod) {//news that affect the entire stock market
            markNewsMod =  (int)((Math.random()*316) + 50)+day;
            Object[] x = news.stockMarkNews(date.plusDays(day));
            markNewsCent+=Double.parseDouble(x[0].toString());
            System.out.println(markNewsCent);
            newsListView.getItems().add(x[1]);
        }

        ///for computer players to make decisions
        for (Stock stck : marketStocks) {//for each stock it will update the price and put the other price in the histPrice variable in stock objects
            stck.addHist(stck.getPrice());
            stck.changePrice((stck.getResp(stck.getSize() - day) - stck.getPrice()) + ((markNewsCent/100)*stck.getPrice()));
            allStocksLst.getItems().add(stck.getName() + " with price: " + df.format(stck.getPrice()));
        }

        for (Person x: players) {//checks to see if person could get dividends
            if ((day-x.getStartDay())%30==0) {
                for (MyStocks ms:x.getPortfolio()) {
                    for (Stock s: marketStocks) {
                        if (s.getName().equalsIgnoreCase(ms.getName())) {
                            x.moneyChange(s.getDividCent()*ms.getAmtShares());
                            break;
                        }
                    }
                }
            }
            x.getHistMoney().add(x.getMoney());
        }
        updPlayers();

        if (date.toString().equals("2017-12-28")) {//my api only has a certain startpoint and endpoint so this checks for an endpoint
            String temp = " ";
            for (Person x: players) {//adds player summary to a string
                temp += x.getName()+":"+x.getMoney()+" ";
            }
            JOptionPane.showMessageDialog(null, "THE END! Player summaries:" + temp);
            //disables fxml
            showPortScreen();
            toolBar.setDisable(true);
            advTime.setDisable(true);
        }
    }
}