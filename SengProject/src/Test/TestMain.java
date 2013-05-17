package Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.JTable;

import gui.OrderbookTable;
import Selecting_Algothrim.*;

import org.junit.Test;
public class TestMain {
	@Test
	public void testAddTrade () {
		newMomentum testStrategy = new newMomentum();
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.2);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.32);
		testStrategy.addTrade(2.34);
		testStrategy.addTrade(2.52);
		testStrategy.addTrade(2.55);
		testStrategy.addTrade(2.7);
		double epsilon = 0.01;
		assertEquals("Testing for adding 10 trades and correctly calculating the average",2.133  , testStrategy.getAverage(), epsilon);	
		testStrategy.addTrade(2.8);
		assertEquals("Testing for 11th trade added, and correctly ignoring the 1st trade",2.413  , testStrategy.getAverage(), epsilon);	
	}
	
	@Test
	public void testSignal () {
		newMomentum testStrategy = new newMomentum();
		signalObject tempSignal;
		orderObject lastSale = new orderObject( -1, -1);
		orderObject lastBuy = new orderObject( -1, -1);
		assertEquals("Default signal is nothing", "nothing", testStrategy.generateOrderSignal(lastSale, lastBuy).getType());
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.2);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.32);
		testStrategy.addTrade(2.34);
		testStrategy.addTrade(2.52);
		testStrategy.addTrade(2.55);
		testStrategy.addTrade(2.7);
		
		testStrategy.addTrade(1.1);
		testStrategy.addTrade(1.1);
		testStrategy.addTrade(1.2);
		testStrategy.addTrade(1.3);
		testStrategy.addTrade(1.3);
		testStrategy.addTrade(1.32);
		testStrategy.addTrade(1.34);
		testStrategy.addTrade(1.52);
		testStrategy.addTrade(1.55);
		testStrategy.addTrade(1.7);
		tempSignal = testStrategy.generateOrderSignal(lastSale, lastBuy);
		//generate a new average < lastaverage
		
		//testStrategy.generateOrderSignal(lastSale, lastBuy);
		System.out.println(tempSignal.getType());
		assertEquals("Computing buy signal", "buy", tempSignal.getType());
		//System.out.println("Generating a sell signal with Trades..");
		lastSale = new orderObject(10, 1.7);
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.1);
		testStrategy.addTrade(2.2);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.3);
		testStrategy.addTrade(2.32);
		testStrategy.addTrade(2.34);
		testStrategy.addTrade(2.52);
		testStrategy.addTrade(2.55);
		testStrategy.addTrade(2.7);
	
		tempSignal = testStrategy.generateOrderSignal(lastSale, lastBuy);
		//generate a new average > lastaverage
		System.out.println(tempSignal.getType());
		assertEquals("Computing sell signal", "nothing", testStrategy.generateOrderSignal(lastSale, lastBuy).getType());
	}
	@Test
	public void testShareQuantity() {
		newMomentum testStrategy = new newMomentum();
		assertTrue("Default share qty is zero", testStrategy.isShareQuantityZero());
		// not sure how finishShare works
	}
	@Test
	public void testReceipt () {
		newMomentum testStrategy = new newMomentum();
		assertEquals("Default number of Buy Receipts", 0, testStrategy.getBuyReceipt().size());
		assertEquals("Default number of Sell Receipts", 0, testStrategy.getSellReceipt().size());
		testStrategy.getreceiptNumber(1);
		//Default signal is in buy mode
		//First transaction should be a sale
		assertEquals("Added 1 sell receipt", 1, testStrategy.getSellReceipt().size());
	}
	
	//GUI TESTS
	@Test
	public void testOrderBook(){
		OrderbookTable testTableModel = new OrderbookTable();
		JTable testTable = new JTable();
		testTable.setModel(testTableModel);
		Object[][] testData = {
				{"abc", new Long(12), new Double(34), new Integer(56) },
		};
		testTableModel.setData(testData);
		assertEquals("Column Count is 4", 4, testTableModel.getColumnCount());
		assertEquals("Testing getValueAt", "abc", testTableModel.getValueAt(0,0));
		testTableModel.setValueAt("def", 0, 0);
		assertEquals("Testing setValueAt for changed value in column 1", "def", testTableModel.getValueAt(0,0));
		assertEquals("Testing getValueAt ", 56, testTableModel.getValueAt(0,3));
		testTableModel.setValueAt(100, 0, 3);
		assertEquals("Testing getValueAt for changed value in column 4", 100, testTableModel.getValueAt(0,3));
		assertEquals("Testing for Row count", 1, testTableModel.getRowCount());
		Object[][]testData2 = {
				{"abc", new Long(12), new Double(34), new Integer(56) },
				{"abc", new Long(12), new Double(34), new Integer(56) },
				{"def", new Long(11), new Double(87), new Integer(41) }
		};
		testTableModel.setData(testData2);
		assertEquals("Testing for increased Row count", 3, testTableModel.getRowCount());		
		
	}
	
}
