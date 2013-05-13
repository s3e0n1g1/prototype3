package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.*; 
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import Selecting_Algothrim.MomentumStrategy;
import Selecting_Algothrim.Strategy;
import Selecting_Algothrim.Trade;
import Trading_Engine.myDatabase;
import Trading_Engine.myTrade;

//commit
// then push

public class Mainmenu  extends JFrame{
	public File csv = null;
	public JTextArea console;
	public JComboBox choosestrat;		
	public myDatabase myDB;
	public Mainmenu(myDatabase db) { 
		myDB = db;
		JPanel pane = new JPanel();
		//pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		Container con = this.getContentPane();
		con.add(pane); 
		setTitle("Prototype 2"); 
		//setLayout(null);
		setSize(800,600);
		setLocationRelativeTo( null );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pane.setBorder(new EmptyBorder(10, 0, 0, 0) );

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
								myDB.insertFromFile(csv);
							} catch (Exception e) {
								console.append("Cannot insert csv to database\n");
							}
							String tmp = myDB.getRowCount();
							console.append(tmp);
							console.append("CSV loaded.\n Please select a strategy.\n");
						}
					}
				});
		pane.add(loadfile);		
		JLabel select= new JLabel("Selected Strategy");
		pane.add(select);

		String[] strategies = {"Momentum"};
		choosestrat = new JComboBox(strategies);
		choosestrat.setSelectedIndex(0);
		choosestrat.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXX");
		pane.add(choosestrat);
		choosestrat.setVisible(true);

		JButton runstrategy = new JButton("Run Strategy");
		pane.add(runstrategy);
		runstrategy.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (csv == null){
							console.append("Please load a csv file before running\n");
						} else {
							console.append("Running strategy " + choosestrat.getSelectedItem().toString()+ "\n");
						
							if (choosestrat.getSelectedIndex() == 0) { //momentum
								//todo: move strategy related functions to strategy package
								myTrade rs = myDB.getTrade("SELECT * FROM trade_list ORDER BY Entry_Time DESC limit 1000;");							
								if(rs.getLength() > 0){
									//int i = 0;
									double result;
									
									MomentumStrategy ms = new MomentumStrategy();
									ms.runStrategy(rs.getAllPrice());
									result = ms.evaluteTheStrategy();
									/*
									Strategy momentum = new Strategy();
									Trade fst = new Trade(null, null, null, Double.parseDouble(a[0]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
									Trade snd = new Trade(null, null, null, Double.parseDouble(a[1]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
									i = 1;			
									while (i < 19){
										momentum.getReturn(fst, snd);
										fst = snd;
										i = i + 1;
										snd = new Trade(null, null, null,Double.parseDouble(a[i]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
									}*/
									console.append("Average return of " + Double.toString(result) + "\n");
									String signal = "Buy";

									if (result > 0.0)
										signal = "Sell";
									console.append("Evaluating strategy based on: "+ signal + " Signal \n");
								} else {
									console.append("rs null");
								}
							}
							//==	
						}
					}
				});


		console = new JTextArea("Prototype 2 Loaded. Please load a CSV file.\n");
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		console.setEditable(false);
		console.setMargin(new Insets(10,10,10,10));
		JScrollPane consoletext = new JScrollPane(console); 
		consoletext.setPreferredSize(new Dimension(550, 450));
		consoletext.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane.add(consoletext);

		JButton quitButton = new JButton("Quit");
		quitButton.setBounds(50, 60, 80, 30);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
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
		//pane.add(quitButton);
		setVisible(true);		
	}

}
