package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Person;
import cz.muni.fi.pv168.hotel.PersonManager;
import cz.muni.fi.pv168.hotel.PersonManagerImpl;
import cz.muni.fi.pv168.hotel.Rent;
import cz.muni.fi.pv168.hotel.RentManager;
import cz.muni.fi.pv168.hotel.RentManagerImpl;
import cz.muni.fi.pv168.hotel.Room;
import cz.muni.fi.pv168.hotel.RoomManager;
import cz.muni.fi.pv168.hotel.RoomManagerImpl;
import cz.muni.fi.pv168.hotel.gui.model.RentTableModel;
import cz.muni.fi.pv168.hotel.gui.verifier.DateRequiredInputVerifier;
import cz.muni.fi.pv168.hotel.gui.verifier.NumericRequiredInputVerifier;
import cz.muni.fi.pv168.hotel.gui.verifier.RequiredInputVerifier;
import cz.muni.fi.pv168.hotel.util.StringUtils;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kurocenko
 */
public class RentForm extends JPanel {

    private static final int CHECK_COLUMN_INDEX = 5;
    private static final int ID_COLUMN_INDEX = -1;
    private RentManager rentManager;
    private RoomManager roomManager;
    private PersonManager personManager;
    private Long currentlyUpdatedEntityId = null;

    private JPanel panel1;
    private JButton cancelButton;
    private JLabel roomabel;
    private JLabel personLabel;
    private JLabel startLabel;
    private JLabel endLabel;
    private JLabel numberLabel;
    private JComboBox<Room> roomCombo;
    private JComboBox<Person> personCombo;
    private JComboBox<Integer> numberCombo;
    private JTextField startField;
    private JTextField endField;
    private JTable rentTable;
    private JButton deleteButton;
    private JButton okButton;


    public RentForm() {
        initManagers();
        initVerifiers();


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
            }
        });
        cancelButton.setText(ResourceBundleProvider.getMessage("button.cancel"));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = validateFields(
                        new JTextField[]{startField, endField},
                        new JLabel[]{startLabel, endLabel}
                );

                if (!errors.isEmpty()) {
                    JOptionPane.showMessageDialog(RentForm.this, StringUtils.printDelimited(errors, "\n"));
                } else {

                    Rent room = assembleRentFromInput();
                    String message;
                    new CreateRentWorker(room).execute();
                    message = ResourceBundleProvider.getMessage("room.msg.create.success", room.getRoom().getNumber(), room.getPerson().getName());

                    JOptionPane.showMessageDialog(RentForm.this, message);
                }
            }
        });
        okButton.setText(ResourceBundleProvider.getMessage("button.ok"));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Rent> checkedRooms = getCheckedRents();

                if (checkedRooms.isEmpty()) {
                    JOptionPane.showMessageDialog(RentForm.this, ResourceBundleProvider.getMessage("error.noselected"));
                    return;
                }

                int chosenOption = JOptionPane.showConfirmDialog(RentForm.this, ResourceBundleProvider.getMessage("room.delete.question", checkedRooms.size()), null, JOptionPane.YES_NO_OPTION);
                if (chosenOption == JOptionPane.YES_OPTION) {
                    new DeleteActionRentWorker(checkedRooms).execute();
                }
            }
        });

        deleteButton.setText(ResourceBundleProvider.getMessage("button.delete"));

        roomCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaintNumberCombo((Room) roomCombo.getSelectedItem());
            }
        });

        roomabel.setText(ResourceBundleProvider.getMessage("rent.room"));
        personLabel.setText(ResourceBundleProvider.getMessage("rent.person"));
        numberLabel.setText(ResourceBundleProvider.getMessage("rent.number"));
        startLabel.setText(ResourceBundleProvider.getMessage("rent.start"));
        endLabel.setText(ResourceBundleProvider.getMessage("rent.end"));

        new LoadAllPersonWorker().execute();
        new LoadAllRoomWorker().execute();

        reloadRentTable();
    }

    private List<Rent> getCheckedRents() {
        int rowCount = rentTable.getModel().getRowCount();
        List<Rent> checkedRentss = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Boolean isChecked = (Boolean) rentTable.getModel().getValueAt(i, CHECK_COLUMN_INDEX);

            if (isChecked) {
                Rent rent = (Rent) rentTable.getModel().getValueAt(i, ID_COLUMN_INDEX);
                checkedRentss.add(rent);
            }
        }
        return checkedRentss;
    }

    private Rent assembleRentFromInput() {
        Long id = currentlyUpdatedEntityId;
        Room room = (Room) roomCombo.getSelectedItem();
        Person person = (Person) personCombo.getSelectedItem();
        LocalDate start = StringUtils.parseDate(startField.getText());
        LocalDate end = StringUtils.parseDate(endField.getText());
        Integer number = (Integer) numberCombo.getSelectedItem();

        BigDecimal price = room.getPricePerDay().multiply(new BigDecimal(StringUtils.getDayDifference(start, end)));
        return new Rent(id, price, room, person, start, end, null, number);
    }

    private DataSource getDataSource() {
        return DataSourceProvider.getDataSource();
    }

    private void initManagers() {

        roomManager = new RoomManagerImpl(getDataSource());
        personManager = new PersonManagerImpl(getDataSource());
        RentManagerImpl manager = new RentManagerImpl(getDataSource());
        manager.setPersonManager(personManager);
        manager.setRoomManager(roomManager);
        rentManager = manager;
    }

    private void initVerifiers() {
        startField.setInputVerifier(new DateRequiredInputVerifier());
        endField.setInputVerifier(new DateRequiredInputVerifier());
    }

    private void clearAllFields() {
        clearFields(startField, endField);
    }

    private void clearFields(JTextField ... fields) {
        for (JTextField field : fields) {
            field.setText("");
            field.setBackground(Color.WHITE);
        }
    }

    private void reloadRentTable() {
        new LoadAllRentWorker().execute();
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
        roomCombo = new JComboBox<>();
        personCombo = new JComboBox<>();
        numberCombo = new JComboBox<>();
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
            reloadRentTable();
        }
    }

    /**
     * Swing worker for creating room
     */
    private class CreateRentWorker extends ActionRoomWorker<Rent> {

        private CreateRentWorker(Rent rent) {
            super(rent);
        }

        @Override
        protected Void doInBackground() throws Exception {
            rentManager.createRent(getInput());
            return null;
        }
    }

    /**
     * Swing worker for deleting room
     */
    private class DeleteActionRentWorker extends ActionRoomWorker<List<Rent>> {

        private DeleteActionRentWorker(List<Rent> rooms) {
            super(rooms);
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Rent rent : getInput()) {
                rentManager.deleteRent(rent);
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
            roomCombo.setModel(new MyComboBoxModel<>(safeGet()));
            numberCombo.repaint();
            repaintNumberCombo((Room) roomCombo.getSelectedItem());
        }
    }

    private class LoadAllRentWorker extends SafeSwingWorker<List<Rent>, Void> {

        @Override
        protected List<Rent> doInBackground() throws Exception {
            return rentManager.findAllRents();
        }

        @Override
        protected void done() {
            rentTable.setModel(new RentTableModel(rentManager.findAllRents()));
            rentTable.repaint();
        }
    }

    private void repaintNumberCombo(Room room) {
        if (room != null) {
            ArrayList<Integer> integers = new ArrayList<Integer>();
            for (int i = 1; i <= room.getCapacity(); i++) {
                integers.add(i);
            }
            numberCombo.setModel(new MyComboBoxModel<>(integers));
            numberCombo.repaint();
        }
    }

    /**
     * Swing worker for loading all rooms
     */
    private class LoadAllPersonWorker extends SafeSwingWorker<List<Person>, Void> {

        @Override
        protected List<Person> doInBackground() throws Exception {
            return personManager.findAllPeople();
        }

        @Override
        protected void done() {
            personCombo.setModel(new MyComboBoxModel<>(safeGet()));
            personCombo.repaint();
        }
    }
}
