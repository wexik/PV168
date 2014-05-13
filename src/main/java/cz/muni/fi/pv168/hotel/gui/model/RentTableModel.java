package cz.muni.fi.pv168.hotel.gui.model;

import cz.muni.fi.pv168.hotel.Rent;
import cz.muni.fi.pv168.hotel.gui.ResourceBundleProvider;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.time.LocalDate;
import java.util.List;

/**
 * @author kurocenko
 */
public class RentTableModel implements TableModel {

    public static final int CHECK_COLUMN_INDEX = 5;

    private List<Rent> rents;
    private boolean[] checked;

    public RentTableModel(List<Rent> rents) {
        this.rents = rents;
        this.checked = new boolean[rents.size()];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 5);
    }

    @Override
    public int getRowCount() {
        return rents.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Rent room = rents.get(rowIndex);

        switch (columnIndex) {
            case -1:
                return room;
            case 0:
                return room.getRoom().getNumber();
            case 1:
                return room.getPerson().getName();
            case 2:
                return room.getStartDay();
            case 3:
                return room.getExpectedEndDay();
            case 4:
                return room.getCountOfGuestsInRoom();
            case 5:
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
                return ResourceBundleProvider.getMessage("rent.room");
            case 1:
                return ResourceBundleProvider.getMessage("rent.person");
            case 2:
                return ResourceBundleProvider.getMessage("rent.start");
            case 3:
                return ResourceBundleProvider.getMessage("rent.end");
            case 4:
                return ResourceBundleProvider.getMessage("rent.number");
            case 5:
                return ResourceBundleProvider.getMessage("table.check");
            default:
                throw new IndexOutOfBoundsException("No column name for index " + column);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return new Class[]{String.class, String.class, LocalDate.class, LocalDate.class, Integer.class, Boolean.class}[columnIndex];
    }

}
