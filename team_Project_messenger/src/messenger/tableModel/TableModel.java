package messenger.tableModel;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{

	public String[] columnName = {"ID", "¿Ã∏ß"};
	public Object[][] data;
	
	
	@Override
	public int getRowCount() {

		return data.length;
	}

	@Override
	public int getColumnCount() {
	
		return columnName.length;
	}
	
	
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnName[column];
	}
	
	
	

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return data[rowIndex][columnIndex+1];
	}
	
}
