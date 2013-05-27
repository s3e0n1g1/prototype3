package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class LineGraph extends JPanel{
	/* 
	private static final long serialVersionUID = 1L
	  public Graph(String applicationTitle, String chartTitle) {
	        super(applicationTitle);
	        // This will create the dataset 
	        PieDataset dataset = createDataset();
	        // based on the dataset we create the chart
	        JFreeChart chart = createChart(dataset, chartTitle);
	        // we put the chart into a panel
	        ChartPanel chartPanel = new ChartPanel(chart);
	        // default size
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        // add it to our application
	        setContentPane(chartPanel);

	    }    
	*/
    private static XYSeries series2;
    public LineGraph(final String title) {
    	series2 = new XYSeries("Input");
    }
    
    public void finishGraph() {
        //final XYDataset dataset = createDataset();
    	createFakeDataset();
    	final JFreeChart chart = createChart(getDataset());
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 400));
        add(chartPanel);
    }
    
       
    public static void addToDataset(double d, double e) {
    	series2.add(d, e);
    }

    private void createFakeDataset() {
        series2.add(1130.0, 1.0);
        series2.add(1135.0,1.0);
        series2.add(1136.0,1.0);
        series2.add(1159.0, 1.5);
        series2.add(1200.0, 1.7);
        series2.add(1230.0, 1.2);
        series2.add(1230.0, 2.2);
        series2.add(1300.0, 2.4);
        series2.add(1430.0, 1.2);
        series2.add(1435.0, 1.6);
        series2.add(1436.0, 2.3);
        series2.add(1459.0, 2.2);
        series2.add(1500.0, 5.8);
        series2.add(1530.0, 3.3);
        series2.add(1530.0, 2.2);
        series2.add(1600.0, 2.2);
        series2.add(1630.0, 2.6);
        series2.add(1635.0, 3.2);
        series2.add(1636.0, 6.2);
        series2.add(1659.0, 4.2);
        series2.add(1700.0, 2.3);
        series2.add(1730.0, 3.1);
        series2.add(1830.0, 3.2);
        series2.add(1900.0, 3.3);        
    }
    
    public XYDataset getDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(new XYSeries("Day 1"));
        dataset.addSeries(series2);
        dataset.addSeries(new XYSeries("Day 3"));      
        return dataset;
    }
    

    private JFreeChart createChart(final XYDataset dataset) {
        
     
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Time graph of trades",   // title
            "Time",                           // x axis label
            "Return",                          // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);
            
        return chart;       
    }
}