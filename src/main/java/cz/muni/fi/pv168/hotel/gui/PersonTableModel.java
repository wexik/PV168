package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Person;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author kurocenko
 */
public class PersonTableModel implements TableModel {

    public static final int CHECK_COLUMN_INDEX = 3;

    private List<Person> people;
    private boolean[] checked;

    public PersonTableModel(List<Person> people) {
        this.people = people;
        this.checked = new boolean[people.size()];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 3);
    }

    @Override
    public int getRowCount() {
        return people.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person person = people.get(rowIndex);

        switch (columnIndex) {
            case -1:
                return person;
            case 0:
                return person.getName();
            case 1:
                return person.getAddress();
            case 2:
                return person.getPhoneNumber();
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
                return "Name";
            case 1:
                return "Address";
            case 2:
                return "Phone";
            case 3:
                return "Check";
            default:
                throw new IndexOutOfBoundsException("No column name for index " + column);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return new Class[]{String.class, String.class, String.class, Boolean.class}[columnIndex];
    }

}
