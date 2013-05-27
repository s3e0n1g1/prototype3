package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.*; 

import javax.swing.text.DefaultCaret;

import org.jfree.ui.RefineryUtilities;
import org.junit.runner.Result;


import New.lecMS;
import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
import Trading_Engine.MyAskList;
import Trading_Engine.MyBidList;
import Trading_Engine.OneData;
import Trading_Engine.ResultData;
import Trading_Engine.myDatabase;

public class Mainmenu  extends JFrame{
	public File csv = null;
	public JTextArea console;
	public JComboBox choosestrat;		
	public myDatabase myDB;
	private OrderbookTable buytable;
	private OrderbookTable selltable;
	public Mainmenu(myDatabase db, Result result) { 
		myDB = db;
		buytable = new OrderbookTable();
		selltable = new OrderbookTable();
		JTabbedPane jtb = new JTabbedPane();

		Container con = this.getContentPane();
		//con.add(pane); 
		con.add(jtb);
		JPanel pane = new JPanel();
		jtb.addTab("Orderbook View", pane);
		setTitle("Prototype ATS"); 
		setSize(1100,800);
		setLocationRelativeTo( null );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		//pane.setBorder(new EmptyBorder(5, 0, 0, 0) );
		JPanel menu = new JPanel();
		//menu.setBorder(new EmptyBorder(0, 0, 0, 0) );
		JButton loadfile = new JButton("Load CSV File");
		loadfile.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.append("Loading CSV file...\n");
						JFileChooser chooser = new JFileChooser();
						int returnVal = chooser.showOpenDialog(null);
						if(returnVal == JFileChooser.APPROVE_OPTION){
							csv = chooser.getSelectedFile();
							try {
								console.append(myDB.insertAll(csv,"all_list"));
							} catch (Exception e) {
								console.append("Cannot insert csv to database\n");
							}
							ResultDisplay myResult = new ResultDisplay();
							myResult.setVisible(true);
							//String tmp = myDB.getRowCount();
							//console.append(tmp);
							console.append("CSV loaded.\nPlease select a strategy.\n");
						}
					}
				});
		menu.add(loadfile);		
		JLabel select= new JLabel("Selected Strategy");
		menu.add(select);

		String[] strategies = {"Momentum"};
		choosestrat = new JComboBox(strategies);
		choosestrat.setSelectedIndex(0);
		choosestrat.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXX");
		menu.add(choosestrat);
		choosestrat.setVisible(true);

		JButton runstrategy = new JButton("Run Strategy");
		menu.add(runstrategy);
		runstrategy.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (csv == null){
							console.append("Please load a csv file before running\n");
						} else {
							console.append("Running strategy " + choosestrat.getSelectedItem().toString()+ "\n");

							if (choosestrat.getSelectedIndex() == 0) { //momentum
								//todo: move strategy related functions to strategy package
								//myTrade rs = myDB.getTrade("SELECT * FROM trade_list ORDER BY Entry_Time DESC limit 1000;");
								runNewStrategy();
							}
							//==	
						}
					}
				});

		pane.add(menu);
		console = new JTextArea("");
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		console.setEditable(false);
		console.setMargin(new Insets(10,10,10,10));
		if(result.wasSuccessful()){
			console.append("All system checks passed.\n");
			console.append("Prototype 2 Loaded. Please load a CSV file.\n");
		}else{
			console.append("System checks not passed.\n");
			console.append("Please close this software and contact the developer regarding this issue.\n");
		}
		JScrollPane consoletext = new JScrollPane(console); 
		consoletext.setPreferredSize(new Dimension(600, 450));
		consoletext.setMaximumSize((new Dimension(600, 450)));
		consoletext.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane.add(consoletext);

		JButton quitButton = new JButton("Quit");
		quitButton.setBounds(50, 60, 80, 30);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myDB.closeDatabase();
				System.exit(0);

			}
		});

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				myDB.closeDatabase();
				System.exit(0);
			}
		});

		menu.add(quitButton);

		String originaltimestamp = "2010-07-14 09:00:02";
		Date date= null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originaltimestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String timestamp = new SimpleDateFormat("HH:mm").format(date); // 9:00

		JTable buybook = new JTable();
		buybook.setModel(buytable);
		//buytable.setData(fakedata);
		JScrollPane scrollTable = new JScrollPane(buybook);
		buybook.setFillsViewportHeight(true);
		scrollTable.setPreferredSize(new Dimension(500,120));
		scrollTable.setMaximumSize(new Dimension(500,150));		
		pane.add(scrollTable);
		Object [] fakedata1 = {100, "24:00",new Long(0), new Double(0), new Integer(0)};
		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);
		buytable.addElement(fakedata1);

		JTable sellbook = new JTable();
		sellbook.setModel(selltable);
		//selltable.setData(fakedata);
		JScrollPane sellTable = new JScrollPane(sellbook);
		buybook.setFillsViewportHeight(true);
		sellTable.setPreferredSize(new Dimension(500,120));
		sellTable.setMaximumSize(new Dimension(500,150));		

		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		selltable.addElement(fakedata1);
		console.append("To add element to table do\n");
		console.append("selltable.addElement(data value as Object[] array)\n");

		JPanel buypanel = new JPanel();
		buypanel.setLayout(new BoxLayout(buypanel, BoxLayout.PAGE_AXIS));
		buypanel.add(new JLabel ("Buy"));
		buypanel.add(scrollTable);

		JPanel sellpanel = new JPanel();
		sellpanel.add(new JLabel ("Sell"));
		sellpanel.add(sellTable);
		sellpanel.setLayout(new BoxLayout(sellpanel, BoxLayout.PAGE_AXIS));

		pane.add(buypanel);
		pane.add(sellpanel);

		//graphs panel

		setVisible(true);
		/*
		LineGraph returntimegraph = new LineGraph("Today's Trades");
		returntimegraph.pack();
		returntimegraph.setLocationRelativeTo(null);
		returntimegraph.setVisible(true);*/

		/*
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        menubar.add(file);
        JMenuItem quit = new JMenuItem("Quit");
        file.add(quit);
        setJMenuBar(menubar);

		 */

		JMenuBar menubar = new JMenuBar();
		//Error? ImageIcon icon = new ImageIcon(getClass().getResource("exit.png"));

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		/*
        JMenuItem eMenuItem = new JMenuItem("Exit", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_C);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }

        });*/

		//file.add(eMenuItem);

		menubar.add(file);

		setJMenuBar(menubar);

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
				console.append("Total lines read : " + count + "\n");
				console.append("Strategy generate " + buySig + " buy signals.\n");
				console.append("Strategy generate " + sellSig + " sell signals.\n");
				console.append("Profit gain: " + profit + "\n");
				myDB.printTwoList();
				myDB.closeTwoList();
			}else{
				System.out.println("set equals null");
			}
			ResultSet bidleft = myDB.getResultSet("SELECT count(*) FROM bid_list;");
			ResultSet askleft = myDB.getResultSet("SELECT count(*) FROM ask_list;");
			if(bidleft!=null){
				if(bidleft.next()){
					console.append("bid_list left with " + bidleft.getString(1) + " lines.\n");
				}
			}
			if(askleft!=null){
				if(askleft.next()){
					console.append("ask_list left with " + askleft.getString(1) + " lines.\n");
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
			console.append("Total lines read : " + count + "\n");
			console.append("Total lines matched : " + completedTrade.size() + "\n");
			console.append("Total lines update : " + updateLines + "\n");
			console.append("Total lines delete : " + deleteLines + "\n");
			console.append("bid list contains " +  myBidList.getLength() + ".\n");
			console.append("ask list contains " +  myAskList.getLength() + ".\n");
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
