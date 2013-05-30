package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import New.lecMS;
import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
import Trading_Engine.GraphData;
import Trading_Engine.MyAskList;
import Trading_Engine.MyBidList;
import Trading_Engine.ResultData;
import Trading_Engine.myDatabase;

public class StrategySelected extends JFrame {
	//public static myDatabase myDB;
	private LinkedList<ResultData> resultTrade;
	private LinkedList<String> strategyResult;
	private LinkedList<ResultData> allCompletedTrade;
	private LinkedList<GraphData> askFirstList;
	private LinkedList<GraphData> bidFirstList;
	private LinkedList<GraphData> strategyAsk;
	private LinkedList<GraphData> strategyBid;
	public StrategySelected(LinkedList<String> runResult, LinkedList<ResultData> allTrades, LinkedList<GraphData> allFirstAskList, LinkedList<GraphData> allFirstBidList, LinkedList<GraphData> allStraAsk, LinkedList<GraphData> allStraBid){
		strategyResult = runResult;
		allCompletedTrade = allTrades;
		askFirstList = allFirstAskList;
		bidFirstList = allFirstBidList;
		strategyAsk = allStraAsk;
		strategyBid = allStraBid;
		JTabbedPane jtb = new JTabbedPane();
		Container con = this.getContentPane(); 
		con.add(jtb);
		setTitle("Momentum strategy used"); 
		setSize(800,600);
		setLocationRelativeTo(null);

		setResizable(false);

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem quit = new JMenuItem("Close");

		menubar.add(file);
		file.add(quit);

		setJMenuBar(menubar);

		jtb.addTab("Analysis", analysisPanel());
		jtb.addTab("All Trades", orderbookPanel());
		jtb.addTab("Strategy Trades", strategyTradePanel());
		jtb.addTab("Strategy Graph", graphPanel());
		jtb.addTab("Best Price Graph", graphPanelLines());

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		setVisible(true);
	}

	public JLabel LinesRead;
	public JLabel MatchedLines;
	public JLabel StrategyMatched;
	public JLabel UpdatedLines;
	public JLabel DeletedLines;
	public JLabel BidList;
	public JLabel AskList;

	private JPanel analysisPanel() {
		JPanel toppanel = new JPanel();
		JPanel analysispanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();

		Dimension d = new Dimension(200,150);
		panel.setLayout((new BoxLayout(panel, BoxLayout.PAGE_AXIS)));

		panel.setSize(d);
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);

		panel2.setLayout((new BoxLayout(panel2, BoxLayout.PAGE_AXIS)));
		panel2.setSize(d);
		panel2.setPreferredSize(d);
		panel2.setMaximumSize(d);


		LinesRead = new JLabel("lines read");
		MatchedLines = new JLabel("matched");
		StrategyMatched = new JLabel("strategy matched");
		UpdatedLines = new JLabel("updated");
		DeletedLines = new JLabel("delete text");
		BidList = new JLabel("total text");
		AskList = new JLabel("tradelines text");

		panel.add(new JLabel("Total lines:"));
		panel.add(new JLabel("Total Lines matched:"));	
		panel.add(new JLabel("Strategy generated matched:"));	
		panel.add(new JLabel("Lines update:"));	
		panel.add(new JLabel("Lines deleted:"));
		panel.add(new JLabel("Bid list contains"));	
		panel.add(new JLabel("Ask list contains"));

		panel2.add(LinesRead);
		panel2.add(MatchedLines);
		panel2.add(StrategyMatched);
		panel2.add(UpdatedLines);
		panel2.add(DeletedLines);
		panel2.add(BidList);
		panel2.add(AskList);

		LinesRead.setText(strategyResult.get(0));
		MatchedLines.setText(strategyResult.get(1));
		StrategyMatched.setText(strategyResult.get(2));
		UpdatedLines.setText(strategyResult.get(3));
		DeletedLines.setText(strategyResult.get(4));
		BidList.setText(strategyResult.get(5));
		AskList.setText(strategyResult.get(6));

		analysispanel.add(panel);
		analysispanel.add(panel2);

		toppanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;	
		analysispanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		toppanel.add(analysispanel,c);
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 2;
		c.gridx = 1;
		c.gridy = 0;
		toppanel.add(new JPanel(),c);

		return toppanel;
	}

	private OrderbookTable ordertable;
	
	private OrderbookTableNew strategyTable;

	private JPanel orderbookPanel() {
		JPanel panel = new JPanel();
		ordertable = new OrderbookTable();

		JTable buybook = new JTable();
		buybook.setModel(ordertable);

		for(int i = 0; i < allCompletedTrade.size(); i++){
			Object [] fakedata1 = {allCompletedTrade.get(i).getBuyID(),allCompletedTrade.get(i).getAskID(),
					"$ " + allCompletedTrade.get(i).getPrice(), allCompletedTrade.get(i).getVol()
					, allCompletedTrade.get(i).getTime()};
			ordertable.addElement(fakedata1);
		}


		Dimension d = new Dimension (650,500);

		JScrollPane scrollTable = new JScrollPane(buybook);
		buybook.setFillsViewportHeight(true);
		scrollTable.setPreferredSize(d);
		scrollTable.setMaximumSize(d);	

		panel.add(scrollTable);
		return panel;
	}

	private JPanel strategyTradePanel() {
		JPanel panel = new JPanel();
		strategyTable = new OrderbookTableNew();

		JTable buybook = new JTable();
		buybook.setModel(strategyTable);

		for(int i = 0; i < allCompletedTrade.size(); i++){
			if(allCompletedTrade.get(i).getBuyID() < 0){
				Object [] fakedata1 = {allCompletedTrade.get(i).getBuyID(),allCompletedTrade.get(i).getAskID(),
						"$ " + allCompletedTrade.get(i).getPrice(), allCompletedTrade.get(i).getVol()
						, allCompletedTrade.get(i).getTime(),0};
				strategyTable.addElement(fakedata1);
			}else if(allCompletedTrade.get(i).getAskID() < 0){
				double tmpProfit = (allCompletedTrade.get(i).getPrice() - allCompletedTrade.get(i-1).getPrice())/allCompletedTrade.get(i).getPrice();
				Object [] fakedata1 = {allCompletedTrade.get(i).getBuyID(),allCompletedTrade.get(i).getAskID(),
						"$ " + allCompletedTrade.get(i).getPrice(), allCompletedTrade.get(i).getVol()
						, allCompletedTrade.get(i).getTime(),tmpProfit};
				strategyTable.addElement(fakedata1);
			}
		}


		Dimension d = new Dimension (650,500);

		JScrollPane scrollTable = new JScrollPane(buybook);
		buybook.setFillsViewportHeight(true);
		scrollTable.setPreferredSize(d);
		scrollTable.setMaximumSize(d);	

		panel.add(scrollTable);
		return panel;
	}

	private JPanel graphPanel(){
		JPanel panel = new JPanel();
		JPanel graphpanel = new JPanel();
		LineGraph returntimegraph = new LineGraph("Strategy Generated");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		for(int i = 0; i < allCompletedTrade.size();i++){
			if(allCompletedTrade.get(i).getAskID() < 0 || allCompletedTrade.get(i).getBuyID() < 0){
				Time tmp = allCompletedTrade.get(i).getTime();
				double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
				LineGraph.addToDataset2(finishTime,allCompletedTrade.get(i).getPrice());
			}
		}

		for(int i = 0; i < strategyAsk.size();i++){
			Time tmp = strategyAsk.get(i).getTime();
			double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
			LineGraph.addToDataset3(finishTime,strategyAsk.get(i).getPrice());
		}

		for(int i = 0; i < strategyBid.size();i++){
			Time tmp = strategyBid.get(i).getTime();
			double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
			LineGraph.addToDataset(finishTime,strategyBid.get(i).getPrice());
		}


		returntimegraph.finishGraph(true);
		returntimegraph.setVisible(true);
		graphpanel.add(returntimegraph);

		JPanel config = new JPanel();
		config.setLayout((new BoxLayout(config, BoxLayout.PAGE_AXIS)));
		config.add(new JLabel("Configure Display"));
		JButton updategraph = new JButton("Update");
		updategraph.setSize(new Dimension(150,75));
		config.add(updategraph);
		config.setBorder(new EmptyBorder(0, 0, 0, 100)) ;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(graphpanel,c);
		//panel.add(config);
		return panel;
	}

	private JPanel graphPanelLines(){

		JPanel panel = new JPanel();
		JPanel graphpanel = new JPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//create new graph and data set
		LineGraph returntimegraph = new LineGraph("Best Price Graph");
		//add plots to the graph


		//LineGraph.addToDataset(546.0, 4.6);
		//loop through database
		for(int i = 0; i < allCompletedTrade.size();i++){
			Time tmp = allCompletedTrade.get(i).getTime();
			double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
			LineGraph.addToDataset2(finishTime,allCompletedTrade.get(i).getPrice());
		}

		for(int i = 0; i < askFirstList.size();i++){
			Time tmp = askFirstList.get(i).getTime();
			double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
			LineGraph.addToDataset3(finishTime,askFirstList.get(i).getPrice());
		}

		for(int i = 0; i < bidFirstList.size();i++){
			Time tmp = bidFirstList.get(i).getTime();
			double finishTime = tmp.getHours() + (tmp.getMinutes()/60.0);
			LineGraph.addToDataset(finishTime,bidFirstList.get(i).getPrice());
		}

		//finalise dataset for graph
		returntimegraph.finishGraph(false);
		returntimegraph.setVisible(true);
		graphpanel.add(returntimegraph);

		//Configurations for graph, not sure if can be done

		JPanel config = new JPanel();
		config.setLayout((new BoxLayout(config, BoxLayout.PAGE_AXIS)));
		config.add(new JLabel("Configure Display"));
		JButton updategraph = new JButton("Update");
		updategraph.setSize(new Dimension(150,75));
		config.add(updategraph);
		config.setBorder(new EmptyBorder(0, 0, 0, 100)) ;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(graphpanel,c );
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 2;
		//panel.add(config);  <--- uncomment to display on gui

		return panel;
	}

}
