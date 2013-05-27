package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import New.lecMS;
import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
import Trading_Engine.MyAskList;
import Trading_Engine.MyBidList;
import Trading_Engine.ResultData;
import Trading_Engine.myDatabase;

public class ResultDisplay extends JFrame {
	public static myDatabase myDB;
	public ResultDisplay(String frametitle, myDatabase db){
		myDB = db;
		JTabbedPane jtb = new JTabbedPane();
		Container con = this.getContentPane(); 
		con.add(jtb);
		setTitle(frametitle); 
		setSize(800,600);
		setLocationRelativeTo(null);

		setResizable(false);

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem match = new JMenuItem("Run Matching");
		JMenuItem quit = new JMenuItem("Close");

		file.add(match);
		menubar.add(file);
		file.add(quit);

		JMenu strategy = new JMenu("Strategy");
		JMenuItem momstrategy = new JMenuItem("Run Momentum Strategy");
		JMenuItem revstrategy = new JMenuItem("Run Momentum Reversion Strategy");
		strategy.add(momstrategy);
		strategy.add(revstrategy);
		menubar.add(strategy);
		setJMenuBar(menubar);

		jtb.addTab("Analysis", analysisPanel());
		jtb.addTab("Orderbook", orderbookPanel());
		jtb.addTab("Graph", graphPanel());

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		momstrategy.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {			
						runNewStrategy();						
					}
				}
				);	

		setVisible(true);
	}

	private JLabel AmendLines;
	private JLabel EnterLines;
	private JLabel DeleteLines;
	private JLabel TradeLines;
	private JLabel ReturnCalculated;
	private JLabel TotalLines;

	private JPanel analysisPanel() {
		JPanel analysispanel = new JPanel();
		JPanel panel = new JPanel();
		analysispanel.add(panel);
		AmendLines = new JLabel("amend text");
		EnterLines = new JLabel("enter text");
		DeleteLines = new JLabel("delete text");
		TradeLines = new JLabel("tradelines text");
		ReturnCalculated = new JLabel();
		TotalLines = new JLabel("total text");
		panel.setLayout((new BoxLayout(panel, BoxLayout.PAGE_AXIS)));
		Dimension d = new Dimension(150,100);
		panel.setSize(d);
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);

		panel.add(new JLabel("AMEND consist of"));
		panel.add(new JLabel("ENTER consist of"));		
		panel.add(new JLabel("DELETE consist of"));		
		panel.add(new JLabel("TRADE consist of"));		
		panel.add(new JLabel("Total lines consist of"));	
		panel.add(new JLabel("Return"));

		ReturnCalculated.setText("212");

		JPanel panel2 = new JPanel();	
		panel2.setLayout((new BoxLayout(panel2, BoxLayout.PAGE_AXIS)));
		panel2.setSize(d);
		panel2.setPreferredSize(d);
		panel2.setMaximumSize(d);

		panel2.add(AmendLines);
		panel2.add(EnterLines);
		panel2.add(DeleteLines);
		panel2.add(TradeLines);
		panel2.add(TotalLines);
		panel2.add(ReturnCalculated);
		analysispanel.add(panel2);
		return analysispanel;
	}

	private OrderbookTable buytable;
	private OrderbookTable selltable;

	private JPanel orderbookPanel() {
		JPanel panel = new JPanel();
		buytable = new OrderbookTable();
		selltable = new OrderbookTable();
		String originaltimestamp = "2010-07-14 09:00:02";
		Date date= null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originaltimestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String timestamp = new SimpleDateFormat("HH:mm").format(date); // 9:00

		JTable buybook = new JTable();
		JTable sellbook = new JTable();

		buybook.setModel(buytable);
		sellbook.setModel(selltable);



		Object [] fakedata1 = {100, "12:00",new Long(0), new Double(0), new Integer(0)};

		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);

		Dimension d = new Dimension (500,150);

		JScrollPane scrollTable = new JScrollPane(buybook);
		buybook.setFillsViewportHeight(true);
		scrollTable.setPreferredSize(d);
		scrollTable.setMaximumSize(d);	

		JScrollPane sellTable = new JScrollPane(sellbook);
		buybook.setFillsViewportHeight(true);
		sellTable.setPreferredSize(d);
		sellTable.setMaximumSize(d);		

		JPanel buypanel = new JPanel();
		buypanel.setLayout(new BoxLayout(buypanel, BoxLayout.PAGE_AXIS));
		buypanel.add(new JLabel ("Buy"));
		buypanel.add(scrollTable);

		JPanel sellpanel = new JPanel();
		sellpanel.setLayout(new BoxLayout(sellpanel, BoxLayout.PAGE_AXIS));
		sellpanel.add(new JLabel ("Sell"));
		sellpanel.add(sellTable);


		panel.add(buypanel);
		panel.add(sellpanel);
		return panel;
	}

	private JPanel graphPanel(){
		JPanel panel = new JPanel();
		LineGraph returntimegraph = new LineGraph("Trades");
		LineGraph.addToDataset(200.0, 1.2);
		returntimegraph.finishGraph();
		returntimegraph.setVisible(true);
		panel.add(returntimegraph);
		panel.add(new JLabel("sdfsdfdsfdalgkadj;lfgkj"));
		return panel;
	}

	protected void runStrategy() {
		try {
			ResultSet set = myDB.getResultSet("SELECT * FROM all_list;");
			if(set != null){
				int count = 0;
				String tmp;
				String tmpType;
				double tmpPrice;
				int tmpVol;
				long tmpID = 0;
				int buySig = 0;
				int sellSig = 0;
				double profit = 0;
				int tmpCount1 = 0;
				int tmpCount2 = 0;
				newMomentum moment = new newMomentum();
				signalObject tempSignal;
				signalObject lastBuySig = null;
				orderObject lastSale = new orderObject( -1, -1);
				orderObject lastBuy = new orderObject( -1, -1);
				myDB.initTwoList();
				while (set.next()){
					tmp = set.getString(5);
					tmpType = set.getString(14);
					if(tmp.equalsIgnoreCase("ENTER")){
						tmpPrice = set.getDouble(6);
						tmpVol = set.getInt(7);
						moment.addTrade(tmpPrice);
						tempSignal = moment.generateOrderSignal(lastSale, lastBuy);
						if(tmpType.equalsIgnoreCase("B")){
							tmpID = set.getLong(12);
							lastBuy = new orderObject(tmpVol,tmpPrice);
							myDB.insertBidList(tmpID,tmpPrice,tmpVol);
						}else if(tmpType.equalsIgnoreCase("A")){
							tmpID = set.getLong(13);
							lastSale = new orderObject(tmpVol,tmpPrice);
							myDB.insertAskList(tmpID,tmpPrice,tmpVol);
						}
						if(tempSignal.getType().equalsIgnoreCase("buy")){
							buySig++;
							moment.getreceiptNumber(buySig);
							lastBuySig = tempSignal;
							//System.out.println("buy generated - count " + count);
						}else if(tempSignal.getType().equalsIgnoreCase("sell")){
							moment.getreceiptNumber(sellSig);
							profit += (tempSignal.getPrice() - lastBuySig.getPrice());
							sellSig++;
							//System.out.println("sell generated - count " + count);
						}
					}else if (tmp.equalsIgnoreCase("AMEND")){

						tmpPrice = set.getLong(12);
						tmpVol = set.getInt(7);
						if(tmpType.equalsIgnoreCase("B")){
							tmpID = set.getLong(12);
							myDB.updateBidList(tmpID, tmpPrice, tmpVol);
						}else if(tmpType.equalsIgnoreCase("A")){
							tmpID = set.getLong(13);
							myDB.updateBidList(tmpID, tmpPrice, tmpVol);
						}
					}else if (tmp.equalsIgnoreCase("DELETE")){

						if(tmpType.equalsIgnoreCase("B")){
							tmpID = set.getLong(12);
							myDB.deleteOneFromList(tmpID, "bid_list");
						}else if(tmpType.equalsIgnoreCase("A")){
							tmpID = set.getLong(13);
							myDB.deleteOneFromList(tmpID, "ask_list");
						}
					}
					count++;
				};
				System.out.println("count : " + count);
				Mainmenu.console.append("Total lines read : " + count + "\n");
				Mainmenu.console.append("Strategy generate " + buySig + " buy signals.\n");
				Mainmenu.console.append("Strategy generate " + sellSig + " sell signals.\n");
				Mainmenu.console.append("Profit gain: " + profit + "\n");
				myDB.printTwoList();
				myDB.closeTwoList();
			}else{
				System.out.println("set equals null");
			}
			ResultSet bidleft = myDB.getResultSet("SELECT count(*) FROM bid_list;");
			ResultSet askleft = myDB.getResultSet("SELECT count(*) FROM ask_list;");
			if(bidleft!=null){
				if(bidleft.next()){
					Mainmenu.console.append("bid_list left with " + bidleft.getString(1) + " lines.\n");
				}
			}
			if(askleft!=null){
				if(askleft.next()){
					Mainmenu.console.append("ask_list left with " + askleft.getString(1) + " lines.\n");
				}
			}

			/*
			if(rs.getLength() > 0){
				//int i = 0;
				double result;

				MomentumStrategy ms = new MomentumStrategy();
				ms.runStrategy(rs.getAllPrice());
				result = ms.evaluteTheStrategy();
				console.append("Average return of " + Double.toString(result) + "\n");
				String signal = "Buy";

				if (result > 0.0)
					signal = "Sell";
				console.append("Evaluating strategy based on: "+ signal + " Signal \n");
			} else {
				console.append("rs null");
			}*/
			askleft.close();
			bidleft.close();
			set.close();
		} catch (SQLException e) {
			System.out.println("In Mainmenu/runStrategy : " + e);
		}
	}

	protected void runNewStrategy() {
		try {
			ResultSet set = myDB.getResultSet("SELECT * FROM all_list;");
			MyBidList myBidList = new MyBidList();
			MyAskList myAskList = new MyAskList();
			LinkedList<ResultData> completedTrade = new LinkedList<ResultData>();
			int count = 0;
			String tmp;
			String tmpType;
			double tmpPrice;
			int tmpVol;
			long tmpID = 0;
			int buySig = 0;
			int sellSig = 0;
			double profit = 0;
			int updateLines = 0;
			int deleteLines = 0;
			Time tmpTime;

			lecMS strategy = new lecMS();

			while (set.next()){
				tmp = set.getString(5);
				tmpType = set.getString(14);
				tmpTime = set.getTime(3);
				if(tmp.equalsIgnoreCase("ENTER")){
					tmpPrice = set.getDouble(6);
					tmpVol = set.getInt(7);
					if(tmpType.equalsIgnoreCase("B")){
						tmpID = set.getLong(12);
						insertBidList(myBidList, myAskList, completedTrade,
								tmpPrice, tmpVol, tmpID, tmpTime);
					}else if(tmpType.equalsIgnoreCase("A")){
						tmpID = set.getLong(13);
						insertAskList(myBidList, myAskList, completedTrade,
								tmpPrice, tmpVol, tmpID, tmpTime);
					}

				}else if (tmp.equalsIgnoreCase("AMEND")){
					updateLines++;
					tmpPrice = set.getLong(12);
					tmpVol = set.getInt(7);
					if(tmpType.equalsIgnoreCase("B")){
						tmpID = set.getLong(12);
						myBidList.update(tmpID,tmpPrice,tmpVol,tmpTime);

					}else if(tmpType.equalsIgnoreCase("A")){
						tmpID = set.getLong(13);

					}
				}else if (tmp.equalsIgnoreCase("DELETE")){
					deleteLines++;
					if(tmpType.equalsIgnoreCase("B")){
						tmpID = set.getLong(12);
						myBidList.deleteOne(tmpID);
					}else if(tmpType.equalsIgnoreCase("A")){
						tmpID = set.getLong(13);

					}
				}
				count++;
			};
			//System.out.println("count : " + count);
			Mainmenu.console.append("Total lines read : " + count + "\n");
			Mainmenu.console.append("Total lines matched : " + completedTrade.size() + "\n");
			Mainmenu.console.append("Total lines update : " + updateLines + "\n");
			Mainmenu.console.append("Total lines delete : " + deleteLines + "\n");
			Mainmenu.console.append("bid list contains " +  myBidList.getLength() + ".\n");
			Mainmenu.console.append("ask list contains " +  myAskList.getLength() + ".\n");
			set.close();
		} catch (SQLException e) {
			System.out.println("In Mainmenu/runStrategy : " + e);
		}
	}
	public void insertBidList(MyBidList myBidList, MyAskList myAskList,
			LinkedList<ResultData> completedTrade, double tmpPrice, int tmpVol,
			long tmpID, Time tmpTime) {
		if(myAskList.getLength() > 0){
			long tmpAskFirstID = myAskList.get(0).getID();
			double tmpAskFirstPrice = myAskList.get(0).getPrice();
			int tmpAskFirstVol = myAskList.get(0).getVol();
			Time tmpAskFirstTime = myAskList.get(0).getTime();
			if(tmpPrice >= tmpAskFirstPrice){
				if(tmpVol > tmpAskFirstVol){
					completedTrade.add(new ResultData(tmpID,tmpAskFirstID,tmpAskFirstVol,tmpAskFirstVol,tmpTime));
					myAskList.deleteAtIndex(0);
					tmpAskFirstVol = tmpVol - tmpAskFirstVol;
					insertBidList(myBidList, myAskList,completedTrade, tmpPrice, tmpAskFirstVol ,tmpID, tmpTime);
				}else if(tmpVol == tmpAskFirstVol){
					completedTrade.add(new ResultData(tmpID,tmpAskFirstID,tmpAskFirstPrice,tmpVol,tmpTime));
					myAskList.deleteAtIndex(0);
				}else{
					completedTrade.add(new ResultData(tmpID,tmpAskFirstID,tmpAskFirstPrice,tmpVol,tmpTime));
					tmpAskFirstVol -= tmpVol;
					myAskList.updateFirst(tmpAskFirstID, tmpAskFirstPrice, tmpAskFirstVol, tmpAskFirstTime);
				}
			}else{
				myBidList.add(tmpID,tmpPrice,tmpVol,tmpTime);
			}
		}else{
			myBidList.add(tmpID,tmpPrice,tmpVol,tmpTime);
		}
	}

	public void insertAskList(MyBidList myBidList, MyAskList myAskList,
			LinkedList<ResultData> completedTrade, double tmpPrice, int tmpVol,
			long tmpID, Time tmpTime) {
		if(myBidList.getLength() > 0){
			long tmpBidFirstID = myBidList.get(0).getID();
			double tmpBidFirstPrice = myBidList.get(0).getPrice();
			int tmpBidFirstVol = myBidList.get(0).getVol();
			Time tmpBidFirstTime = myBidList.get(0).getTime();
			if(tmpPrice <= tmpBidFirstPrice){
				if(tmpVol > tmpBidFirstVol){
					completedTrade.add(new ResultData(tmpBidFirstID,tmpID,tmpPrice,tmpBidFirstVol,tmpTime));
					tmpBidFirstVol = tmpVol - tmpBidFirstVol;
					myBidList.deleteAtIndex(0);
					insertBidList(myBidList, myAskList,completedTrade, tmpPrice,tmpBidFirstVol ,tmpID, tmpTime);
				}else if(tmpVol == tmpBidFirstVol){
					completedTrade.add(new ResultData(tmpBidFirstID,tmpID,tmpPrice,tmpVol,tmpTime));
					myBidList.deleteAtIndex(0);
				}else{
					completedTrade.add(new ResultData(tmpBidFirstID,tmpID,tmpPrice,tmpVol,tmpTime));
					tmpBidFirstVol -= tmpVol;
					myBidList.updateFirst(tmpBidFirstID,tmpBidFirstPrice, tmpBidFirstVol, tmpBidFirstTime);
				}
			}else{
				myAskList.add(tmpID,tmpPrice,tmpVol,tmpTime);
			}
		}else{
			myAskList.add(tmpID,tmpPrice,tmpVol,tmpTime);
		}

	}
}
