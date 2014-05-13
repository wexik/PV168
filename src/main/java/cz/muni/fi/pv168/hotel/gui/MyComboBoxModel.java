package cz.muni.fi.pv168.hotel.gui;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

/**
 * @author kurocenko
 */
public class MyComboBoxModel<T> implements ComboBoxModel<T> {

    private List<T> data;
    private T selected = null;

    public MyComboBoxModel(List<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data are null");
        }

        this.data = data;
        if (!data.isEmpty()) {
            selected = data.get(0);
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selected = (T) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public T getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
