package gui;

import javax.swing.table.AbstractTableModel;

public class OrderbookTable extends AbstractTableModel {

	private String[] columnNames =  {"#", "ID", "Price", "Volume"};
	private Object[][] data = {{"", new Long(0), new Double(0), new Integer(0)}};

	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
    public int getColumnCount() {
        return columnNames.length;
    }

	public int getRowCount() {
		return data.length;
	}
	public String getColumnName(int col){
		return columnNames[col];
	}
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
	
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
	    fireTableCellUpdated(row, col);
	}

	public void setData(Object[][] fakedata) {
		data = fakedata;
		fireTableDataChanged();

	}

}
