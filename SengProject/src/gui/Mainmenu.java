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
import New.lecMSMomentum;
import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
import Trading_Engine.MyAskList;
import Trading_Engine.MyBidList;
import Trading_Engine.ResultData;
import Trading_Engine.myDatabase;

public class Mainmenu extends JFrame{
	public static File csv = null;
	public static JTextArea console;
	public JComboBox choosestrat;		
	public static myDatabase myDB;
	private ResultDisplay myResult;
	public Mainmenu(myDatabase db, Result result) { 
		final JFrame frame = this;
		setTitle("Algorithmic Trading System"); 
		setSize(800,600);
		setLocationRelativeTo( null );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		myDB = db;
		Container con = this.getContentPane();
		JPanel pane = new JPanel();
		con.add(pane); 
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		menubar.add(file);
		JMenuItem quit = new JMenuItem("Quit");
		JMenuItem loadcsv = new JMenuItem("Load CSV File");
		file.add(loadcsv);
		file.add(quit);
		JMenu help = new JMenu("Help");		
		//JMenuItem about = new JMenuItem("About");
		JMenuItem howtouse = new JMenuItem("How To Use");
		//help.add(about);
		help.add(howtouse);

		menubar.add(help);
		setJMenuBar(menubar);
		pane.add(consolePanel(result));

		//pane.setBorder(new EmptyBorder(5, 0, 0, 0) );

		howtouse.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event){
						JDialog help = new helpDialog(frame);
					}
				});


		loadcsv.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.append("Loading CSV file...\n");
						JFileChooser chooser = new JFileChooser();
						int returnVal = chooser.showOpenDialog(null);
						if(returnVal == JFileChooser.APPROVE_OPTION){
							csv = chooser.getSelectedFile();
							try {
								myResult = new ResultDisplay("Analysis for " + csv.getName(),myDB,myDB.insertAll(csv,"all_list"));
							} catch (Exception e) {
								console.append("Cannot insert csv to database\n");
							}
							myResult.setVisible(true);
							//runNewStrategy();

							//String tmp = myDB.getRowCount();
							//console.append(tmp);
							console.append("CSV loaded.\nPlease select a strategy.\n");
						}
					}
				});

		quit.addActionListener(new ActionListener() {
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
		setVisible(true);	
	}

	class helpDialog extends JDialog {
		public helpDialog(JFrame frame) {
			super (frame, false);
			JDialog help = new JDialog(frame, false);
			JPanel panel = new JPanel();
			getContentPane().add(panel);
			setResizable(false);
			setLocationRelativeTo(frame);
			setSize(400,220);
			setTitle("Help");
			JTextArea text = new JTextArea ("To open a CSV file, click File - Load a CSV. To run a strategy, a CSV file musted be selected. " +
					"A new window will open including new buttons to select your strategy. The console area will display the list of returns based on the result time after running a strategy. Visual and table aid can be found in the following window.");
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setEditable(false);
			text.setMargin(new Insets(15,15,15,15));
			panel.add(text);
			text.setPreferredSize(new Dimension( 350,150));
			JButton ok = new JButton ("Close");
			panel.add(ok);
			
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setVisible(false);
				}
			});
			setVisible(true);
		}
	}
	private JScrollPane consolePanel (Result result){
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

		return consoletext;
	}

	//DEFAULT STRATEGY IMPLEMENTED 
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

			lecMSMomentum strategy = new lecMSMomentum();

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



			//update jlabels
			//myResult.LinesRead.setText(Integer.toString( count));
			//myResult.enterLines.setText(Integer.toString(completedTrade.size()));
			//myResult.askLines.setText(Integer.toString(updateLines));
			//myResult.bidLines.setText(Integer.toString(deleteLines));
			//myResult.tradeLines.setText(Integer.toString(myAskList.getLength()));
			//myResult.AskList.setText(Integer.toString(myAskList.getLength()));


			//ReturnCalculated.setText("212"); //to change value of labels

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
					myAskList.updateFirst(tmpAskFirstVol);
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
					myBidList.updateFirst(tmpBidFirstVol);
				}
			}else{
				myAskList.add(tmpID,tmpPrice,tmpVol,tmpTime);
			}
		}else{
			myAskList.add(tmpID,tmpPrice,tmpVol,tmpTime);
		}

	}

}