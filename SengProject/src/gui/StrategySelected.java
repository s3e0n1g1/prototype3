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
import Trading_Engine.MyAskList;
import Trading_Engine.MyBidList;
import Trading_Engine.ResultData;
import Trading_Engine.myDatabase;

public class StrategySelected extends JFrame {
	//public static myDatabase myDB;
	public StrategySelected(){
		//myDB = db;
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
		jtb.addTab("Orderbook", orderbookPanel());
		jtb.addTab("Graph", graphPanel());

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
	public JLabel UpdatedLines;
	public JLabel DeletedLines;
	public JLabel BidList;
	public JLabel AskList;
	
	private JPanel analysisPanel() {
		JPanel toppanel = new JPanel();
		JPanel analysispanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		Dimension d = new Dimension(150,100);
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
		UpdatedLines = new JLabel("updated");
		DeletedLines = new JLabel("delete text");
		BidList = new JLabel("total text");
		AskList = new JLabel("tradelines text");
		
		panel.add(new JLabel("Total lines read:"));
		panel.add(new JLabel("Total lines matched:"));	
		panel.add(new JLabel("Total lines update:"));	
		panel.add(new JLabel("Total lines deleted:"));
		panel.add(new JLabel("Bid list contains"));	
		panel.add(new JLabel("Ask list contains"));

		panel2.add(LinesRead);
		panel2.add(MatchedLines);
		panel2.add(UpdatedLines);
		panel2.add(DeletedLines);
		panel2.add(BidList);
		panel2.add(AskList);
		
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
	
	private JPanel orderbookPanel() {
		JPanel panel = new JPanel();
		ordertable = new OrderbookTable();
		
		JTable buybook = new JTable();
		buybook.setModel(ordertable);

		Object [] fakedata1 = {123,245, "$ " + Double.toString(new Double(5.30)), new Integer(43), "9:00"};
		
		ordertable.addElement(fakedata1);
		ordertable.addElement(fakedata1);
		ordertable.addElement(fakedata1);
		ordertable.addElement(fakedata1);
		Dimension d = new Dimension (500,150);

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
		LineGraph returntimegraph = new LineGraph("Trades with Momentum");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		LineGraph.addToDataset(200.0, 3.2);
		LineGraph.addToDataset(230.0, 4.3);
		LineGraph.addToDataset(300.0, 2.4);
		LineGraph.addToDataset(400.0, 5.2);
		LineGraph.addToDataset(810.0, 3.3);
		LineGraph.addToDataset(240.0, 6.3);
		LineGraph.addToDataset(546.0, 1.6);
		returntimegraph.finishGraph();
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
}
