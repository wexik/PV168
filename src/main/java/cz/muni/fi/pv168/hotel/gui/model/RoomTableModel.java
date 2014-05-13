package cz.muni.fi.pv168.hotel.gui.model;

import cz.muni.fi.pv168.hotel.Room;
import cz.muni.fi.pv168.hotel.gui.ResourceBundleProvider;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author mashka
 */
public class RoomTableModel implements TableModel {

    public static final int CHECK_COLUMN_INDEX = 3;

    private List<Room> rooms;
    private boolean[] checked;

    public RoomTableModel(List<Room> rooms) {
        this.rooms = rooms;
        this.checked = new boolean[rooms.size()];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 3);
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room room = rooms.get(rowIndex);

        switch (columnIndex) {
            case -1:
                return room;
            case 0:
                return room.getNumber();
            case 1:
                return room.getCapacity();
            case 2:
                return room.getPricePerDay();
            case 3:
                return checked[rowIndex];
            default:
                throw new IndexOutOfBoundsException("No data for column index " + columnIndex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == CHECK_COLUMN_INDEX) {
            checked[rowIndex] = (Boolean) aValue;
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        System.out.println();
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        System.out.println();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return ResourceBundleProvider.getMessage("room.number");
            case 1:
                return ResourceBundleProvider.getMessage("room.capacity");
            case 2:
                return ResourceBundleProvider.getMessage("room.price");
            case 3:
                return ResourceBundleProvider.getMessage("table.check");
            default:
                throw new IndexOutOfBoundsException("No column name for index " + column);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return new Class[]{String.class, String.class, String.class, Boolean.class}[columnIndex];
    }

}
