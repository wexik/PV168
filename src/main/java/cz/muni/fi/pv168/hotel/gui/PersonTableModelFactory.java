package cz.muni.fi.pv168.hotel.gui;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author kurocenko
 */
public class PersonTableModelFactory {

    public static final String[] COLUMN_NAMES = {"Name", "Address", "Phone", "Check"};
    public static final Class[] COLUMN_CLASSES = {String.class, String.class, String.class, Boolean.class};


    public static TableModel getTableModel(Object[][] values) {

        return new DefaultTableModel(values, COLUMN_NAMES) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return COLUMN_CLASSES[columnIndex];
            }
        };
    }

}
