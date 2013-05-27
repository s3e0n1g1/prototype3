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

import org.jfree.ui.RefineryUtilities;
import org.junit.runner.Result;


import Selecting_Algothrim.newMomentum;
import Selecting_Algothrim.orderObject;
import Selecting_Algothrim.signalObject;
import Trading_Engine.myDatabase;

public class Mainmenu  extends JFrame{
	public static File csv = null;
	public static JTextArea console;
	public JComboBox choosestrat;		
	public static myDatabase myDB;
	public Mainmenu(myDatabase db, Result result) { 
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
		JMenuItem about = new JMenuItem("About");
		JMenuItem howtouse = new JMenuItem("How To Use");
		help.add(about);
		help.add(howtouse);
		
		menubar.add(help);
		setJMenuBar(menubar);
		pane.add(consolePanel(result));
		
		//pane.setBorder(new EmptyBorder(5, 0, 0, 0) );
		
		
		loadcsv.addActionListener(
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
							ResultDisplay myResult = new ResultDisplay("Analysis for " + csv.getName());
							myResult.setVisible(true);
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


}
