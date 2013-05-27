package gui;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

public class ResultDisplay extends JFrame {
	public ResultDisplay(){
		JTabbedPane jtb = new JTabbedPane();
		
		Container con = this.getContentPane();
		//con.add(pane); 
		con.add(jtb);
		JPanel pane = new JPanel();
		
		
		jtb.addTab("Main View", pane);
		setTitle("Result"); 
		setSize(800,600);
		setLocationRelativeTo( null );
		setResizable(false);
		
		LineGraph returntimegraph = new LineGraph("Today's Trades");
		returntimegraph.setVisible(true);
		jtb.addTab("Graph display", returntimegraph);
	}
}
