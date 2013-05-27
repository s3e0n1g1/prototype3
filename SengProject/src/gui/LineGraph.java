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
    public LineGraph(final String title) {


        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(750, 450));
        add(chartPanel);

    }
    
    private XYDataset createDataset() {
        
        final XYSeries series1 = new XYSeries("Day 1");
        series1.add(11.30, 1.5);
        series1.add(11.35, 1.2);
        series1.add(11.36, 1.4);
        series1.add(11.59, 1.5);
        series1.add(12.00, 1.7);
        series1.add(12.30, 1.8);
        series1.add(12.30, 2.2);
        series1.add(13.00, 2.4);
        series1.add(14.30, 1.2);
        series1.add(14.35, 1.6);
        series1.add(14.36, 2.3);
        series1.add(14.59, 2.2);
        series1.add(15.00, 5.8);
        series1.add(15.30, 3.3);
        series1.add(15.30, 2.2);
        series1.add(16.00, 2.8);
        series1.add(16.30, 2.6);
        series1.add(16.35, 3.5);
        series1.add(16.36, 1.2);
        series1.add(16.59, 3.2);
        series1.add(17.00, 5.3);
        series1.add(17.30, 2.1);
        series1.add(18.30, 1.2);
        series1.add(19.00, 6.3);
        final XYSeries series2 = new XYSeries("Day 2");
        series2.add(11.30, 1.0);
        series2.add(11.35, 1.0);
        series2.add(11.36, 1.0);
        series2.add(11.59, 1.5);
        series2.add(12.00, 1.7);
        series2.add(12.30, 1.2);
        series2.add(12.30, 2.2);
        series2.add(13.00, 2.4);
        series2.add(14.30, 1.2);
        series2.add(14.35, 1.6);
        series2.add(14.36, 2.3);
        series2.add(14.59, 2.2);
        series2.add(15.00, 5.8);
        series2.add(15.30, 3.3);
        series2.add(15.30, 2.2);
        series2.add(16.00, 2.2);
        series2.add(16.30, 2.6);
        series2.add(16.35, 3.2);
        series2.add(16.36, 6.2);
        series2.add(16.59, 4.2);
        series2.add(17.00, 2.3);
        series2.add(17.30, 3.1);
        series2.add(18.30, 3.2);
        series2.add(19.00, 3.3);
        final XYSeries series3 = new XYSeries("Day 3");
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);      
        return dataset;
        
    }
    

    private JFreeChart createChart(final XYDataset dataset) {
        
     
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Time graph of today's trades",   // title
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

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    
               
        return chart;
        
    }
}
