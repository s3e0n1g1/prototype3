package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*; 

import javax.swing.text.DefaultCaret;

import org.junit.runner.Result;


import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
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
		JPanel pane = new JPanel();
		Container con = this.getContentPane();
		con.add(pane); 
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
													selltable.setValueAt("1", 1, 0);
													selltable.setValueAt(tmpID, 1, 1);
													selltable.setValueAt(tmpPrice, 1, 2);
													selltable.setValueAt(tmpVol, 1, 3);
												}
												if(tempSignal.getType().equalsIgnoreCase("buy")){
													buySig++;
													moment.getreceiptNumber(buySig);
													lastBuySig = tempSignal;
													if(tmpCount1 == 10){
														tmpCount1 = 0;
													}
													buytable.setValueAt(tmpCount1, tmpCount1, 0);
													buytable.setValueAt(tmpID, tmpCount1, 1);
													buytable.setValueAt(tmpPrice, tmpCount1, 2);
													buytable.setValueAt(tmpVol, tmpCount1, 3);
													tmpCount1++;
													tmpCount1++;
													//System.out.println("buy generated - count " + count);
												}else if(tempSignal.getType().equalsIgnoreCase("sell")){
													moment.getreceiptNumber(sellSig);
													profit += (tempSignal.getPrice() - lastBuySig.getPrice());
													sellSig++;
													if(tmpCount2 == 10){
														tmpCount2 = 0;
													}
													selltable.setValueAt(tmpCount2, tmpCount2, 0);
													selltable.setValueAt(tmpID, tmpCount2, 1);
													selltable.setValueAt(tmpPrice, tmpCount2, 2);
													selltable.setValueAt(tmpVol, tmpCount2, 3);
													tmpCount2++;
													//System.out.println("sell generated - count " + count);
													
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
									
									set.close();
								} catch (SQLException e) {
									System.out.println("In Mainmenu/runStrategy : " + e);
								}
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
		String timestamp = new SimpleDateFormat("H:mm").format(date); // 9:00
		Object[][] fakedata = {
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0),new Integer(0)  },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) },
				{"", new Long(0), new Double(0), new Integer(0) }
		};
		JTable buybook = new JTable();
		buybook.setModel(buytable);
		buytable.setData(fakedata);
		JScrollPane scrollTable = new JScrollPane(buybook);
		buybook.setFillsViewportHeight(true);
		scrollTable.setPreferredSize(new Dimension(500,120));
		scrollTable.setMaximumSize(new Dimension(500,150));		
		pane.add(scrollTable);

		JTable sellbook = new JTable();
		sellbook.setModel(selltable);
		selltable.setData(fakedata);
		JScrollPane sellTable = new JScrollPane(sellbook);
		buybook.setFillsViewportHeight(true);
		sellTable.setPreferredSize(new Dimension(500,120));
		sellTable.setMaximumSize(new Dimension(500,150));		


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

		setVisible(true);		
	}

}
