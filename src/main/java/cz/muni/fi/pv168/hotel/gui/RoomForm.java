package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Room;
import cz.muni.fi.pv168.hotel.RoomManager;
import cz.muni.fi.pv168.hotel.RoomManagerImpl;
import cz.muni.fi.pv168.hotel.gui.model.RoomTableModel;
import cz.muni.fi.pv168.hotel.gui.verifier.NumericRequiredInputVerifier;
import cz.muni.fi.pv168.hotel.gui.verifier.PriceRequiredInputVerifier;
import cz.muni.fi.pv168.hotel.gui.verifier.RequiredInputVerifier;
import cz.muni.fi.pv168.hotel.util.StringUtils;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mashka
 */
public class RoomForm extends JPanel {

    private static final int CHECK_COLUMN_INDEX = 3;
    private static final int ID_COLUMN_INDEX = -1;
    private RoomManager roomManager;
    private Long currentlyUpdatedEntityId = null;

    private JTextField capacityField;
    private JTextField priceField;
    private JLabel capacityLabel;
    private JLabel priceLabel;
    private JButton cancelButton;
    private JButton okButton;
    private JTextField numberField;
    private JLabel numberLabel;
    private JTable roomTable;
    private JButton deleteButton;
    private JButton updateButton;
    private JPanel panelek;
    private JPanel panel1;

    public RoomForm() {

        initManagers();
        initVerifiers();


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
                if (currentlyUpdatedEntityId != null) {
                    new LoadRoomWorker(currentlyUpdatedEntityId).execute();
                }
            }
        });
        cancelButton.setText(ResourceBundleProvider.getMessage("button.cancel"));

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Room> checkedRooms = getCheckedRooms();

                if (checkedRooms.isEmpty()) {
                    JOptionPane.showMessageDialog(RoomForm.this, ResourceBundleProvider.getMessage("error.noselected"));
                } else if (checkedRooms.size() > 1) {
                    JOptionPane.showMessageDialog(RoomForm.this, ResourceBundleProvider.getMessage("error.moreselected"));
                } else {
                    setFieldValues(checkedRooms.get(0));
                }
            }
        });
        updateButton.setText(ResourceBundleProvider.getMessage("button.update"));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = validateFields(
                        new JTextField[]{numberField, capacityField, priceField},
                        new JLabel[]{numberLabel, capacityLabel, priceLabel}
                );

                if (!errors.isEmpty()) {
                    JOptionPane.showMessageDialog(RoomForm.this, StringUtils.printDelimited(errors, "\n"));
                } else {

                    Room room = assembleRoomFromInput();
                    String message;
                    if (room.getId() == null) {
                        new CreateRoomWorker(room).execute();
                        message = ResourceBundleProvider.getMessage("room.msg.create.success", room.getNumber());
                    } else {
                        new UpdateActionRoomWorker(room).execute();
                        message = ResourceBundleProvider.getMessage("room.msg.update.success", room.getNumber());
                    }

                    JOptionPane.showMessageDialog(RoomForm.this, message);
                }
            }
        });
        okButton.setText(ResourceBundleProvider.getMessage("button.ok"));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Room> checkedRooms = getCheckedRooms();

                if (checkedRooms.isEmpty()) {
                    JOptionPane.showMessageDialog(RoomForm.this, ResourceBundleProvider.getMessage("error.noselected"));
                    return;
                }

                int chosenOption = JOptionPane.showConfirmDialog(RoomForm.this, ResourceBundleProvider.getMessage("room.delete.question", checkedRooms.size()), null, JOptionPane.YES_NO_OPTION);
                if (chosenOption == JOptionPane.YES_OPTION) {
                    new DeleteActionRoomWorker(checkedRooms).execute();
                }
            }
        });
        deleteButton.setText(ResourceBundleProvider.getMessage("button.delete"));

        numberLabel.setText(ResourceBundleProvider.getMessage("room.number"));
        capacityLabel.setText(ResourceBundleProvider.getMessage("room.capacity"));
        priceLabel.setText(ResourceBundleProvider.getMessage("room.price"));
        roomTable.setTableHeader(new JTableHeader(new DefaultTableColumnModel()));

        reloadRoomTable();
    }

    private List<Room> getCheckedRooms() {
        int rowCount = roomTable.getModel().getRowCount();
        List<Room> checkedRooms = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Boolean isChecked = (Boolean) roomTable.getModel().getValueAt(i, CHECK_COLUMN_INDEX);

            if (isChecked) {
                Room room = (Room) roomTable.getModel().getValueAt(i, ID_COLUMN_INDEX);
                checkedRooms.add(room);
            }
        }
        return checkedRooms;
    }

    private Room assembleRoomFromInput() {
        Long id = currentlyUpdatedEntityId;
        Integer number = Integer.valueOf(numberField.getText());
        Integer capacity = Integer.valueOf(capacityField.getText());
        BigDecimal price = new BigDecimal(priceField.getText());

        return new Room(id, capacity, number, price);
    }

    private DataSource getDataSource() {
        return DataSourceProvider.getDataSource();
    }

    private void initManagers() {
        roomManager = new RoomManagerImpl(getDataSource());
    }

    private void initVerifiers() {
        capacityField.setInputVerifier(new NumericRequiredInputVerifier());
        priceField.setInputVerifier(new PriceRequiredInputVerifier());
        numberField.setInputVerifier(new NumericRequiredInputVerifier());
    }

    private void setFieldValues(Room room) {
        numberField.setText(String.valueOf(room.getNumber()));
        capacityField.setText(String.valueOf(room.getCapacity()));
        priceField.setText(room.getPricePerDay().toString());
        currentlyUpdatedEntityId = room.getId();
    }

    private void clearAllFields() {
        clearFields(numberField, priceField, capacityField);
    }

    private void clearFields(JTextField ... fields) {
        for (JTextField field : fields) {
            field.setText("");
            field.setBackground(Color.WHITE);
        }
    }

    private void reloadRoomTable() {
        new LoadAllRoomWorker().execute();
    }

    /**
     * Validates fields, paints background if error occures, returns error messages
     */
    private List<String> validateFields(JTextField[] fields, JLabel[] labels) {

        if (fields.length != labels.length) {
            throw new IllegalArgumentException("Count of fields has to be the same as count of labels");
        }

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            InputVerifier inputVerifier = fields[i].getInputVerifier();
            if (!inputVerifier.verify(fields[i])) {
                inputVerifier.shouldYieldFocus(fields[i]); // colorify input field

                if (inputVerifier instanceof NumericRequiredInputVerifier) {
                    errors.add(ResourceBundleProvider.getMessage("error.number", labels[i].getText()));
                } else if (inputVerifier instanceof RequiredInputVerifier) {
                    errors.add(ResourceBundleProvider.getMessage("error.mandatory", labels[i].getText()));
                }
            }
        }

        return errors;
    }

    private void createUIComponents() {
    }


    /**
     * Abstract swing worker for doing create/update/delete action method and refreshing
     * room table and input fields
     *
     * @param <T> type of input parameter of action method
     */
    private abstract class ActionRoomWorker<T> extends SafeSwingWorker<Void, Void> {

        private T input;

        private ActionRoomWorker(T input) {
            this.input = input;
        }

        protected T getInput() {
            return input;
        }

        @Override
        protected void done() {
            currentlyUpdatedEntityId = null;
            clearAllFields();
            reloadRoomTable();
        }
    }

    /**
     * Swing worker for creating room
     */
    private class CreateRoomWorker extends ActionRoomWorker<Room> {

        private CreateRoomWorker(Room room) {
            super(room);
        }

        @Override
        protected Void doInBackground() throws Exception {
            roomManager.createRoom(getInput());
            return null;
        }
    }

    /**
     * Swing worker for updating room
     */
    private class UpdateActionRoomWorker extends ActionRoomWorker<Room> {

        private UpdateActionRoomWorker(Room room) {
            super(room);
        }

        @Override
        protected Void doInBackground() throws Exception {
            roomManager.updateRoom(getInput());
            return null;
        }
    }

    /**
     * Swing worker for deleting room
     */
    private class DeleteActionRoomWorker extends ActionRoomWorker<List<Room>> {

        private DeleteActionRoomWorker(List<Room> rooms) {
            super(rooms);
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Room room : getInput()) {
                roomManager.deleteRoom(room);
            }
            return null;
        }
    }

    /**
     * Swing worker for loading all rooms
     */
    private class LoadAllRoomWorker extends SafeSwingWorker<List<Room>, Void> {

        @Override
        protected List<Room> doInBackground() throws Exception {
            return roomManager.findAllRooms();
        }

        @Override
        protected void done() {
            roomTable.setModel(new RoomTableModel(safeGet()));
            roomTable.repaint();
        }
    }

    /**
     * Swing worker for loading room by ID
     */
    private class LoadRoomWorker extends SafeSwingWorker<Room, Void> {

        private Long roomId;

        private LoadRoomWorker(Long roomId) {
            this.roomId = roomId;
        }

        @Override
        protected Room doInBackground() throws Exception {
            return roomManager.findRoomById(roomId);
        }

        @Override
        protected void done() {
            setFieldValues(safeGet());
        }
    }
}
