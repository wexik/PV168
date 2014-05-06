package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.Person;
import cz.muni.fi.pv168.hotel.PersonManager;
import cz.muni.fi.pv168.hotel.PersonManagerImpl;
import cz.muni.fi.pv168.hotel.gui.verifier.NumericRequiredInputVerifier;
import cz.muni.fi.pv168.hotel.gui.verifier.RequiredInputVerifier;
import cz.muni.fi.pv168.hotel.util.StringUtils;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kurocenko
 */
public class PersonForm extends JPanel {

    private static final int CHECK_COLUMN_INDEX = 3;
    private PersonManager personManager;
    
    private JPanel panel1;
    private JLabel nameLabel;
    private JLabel addressLabel;
    private JButton okButton;
    private JButton cancelButton;
    private JTable personTable;
    private JButton updateButton;
    private JButton deleteButton;
    private JLabel phoneLabel;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;

    public PersonForm() {

        initManagers();

        nameField.setInputVerifier(new RequiredInputVerifier());
        addressField.setInputVerifier(new RequiredInputVerifier());
        phoneField.setInputVerifier(new NumericRequiredInputVerifier());


        // TODO swing worker
        final List<Person> people = personManager.findAllPeople();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = validateFields(
                        new JTextField[]{nameField, phoneField, addressField},
                        new JLabel[]{nameLabel, phoneLabel, addressLabel}
                );

                if (!errors.isEmpty()) {
                    JOptionPane.showMessageDialog(PersonForm.this, StringUtils.printDelimited(errors, "\n"));
                } else {

                    String name = nameField.getText();
                    String phone = phoneField.getText();
                    String address = addressField.getText();

                    // TODO swing worker
                    personManager.createPerson(new Person(null, name, phone, address));

                    clearFields(nameField, phoneField, addressField);
                    JOptionPane.showMessageDialog(PersonForm.this, "Person " + name + " successfuly created");
                    reloadPersonTable();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = personTable.getModel().getRowCount();

                List<Integer> removedIndexes = new ArrayList<>();
                List<Person> toDelete = new ArrayList<Person>();
                for (int i = 0; i < rowCount; i++) {
                    Boolean isChecked = (Boolean) personTable.getModel().getValueAt(i, CHECK_COLUMN_INDEX);

                    if (isChecked) {
                        Person person = (Person) personTable.getModel().getValueAt(i, -1);
                        removedIndexes.add(i);
                        toDelete.add(person);
                    }
                }

                if (toDelete.isEmpty()) {
                    JOptionPane.showMessageDialog(PersonForm.this, "You have to check some people first");
                    return;
                }

                int chosenOption = JOptionPane.showConfirmDialog(PersonForm.this, "Do you really want to remove all selected people (" + toDelete.size() + ")?", null, JOptionPane.YES_NO_OPTION);
                if (chosenOption == JOptionPane.YES_OPTION) {
                    for (Person person : toDelete) {
                        // TODO swing worker
                        personManager.deletePerson(person);
                    }

                    reloadPersonTable();
                }
            }
        });


        personTable.setModel(new PersonTableModel(personManager.findAllPeople()));

    }

    private void reloadPersonTable() {
        // todo swing worker
        List<Person> allPeople = personManager.findAllPeople();
        personTable.setModel(new PersonTableModel(allPeople));
        personTable.repaint();
    }

    private void clearFields(JTextField ... fields) {
        for (JTextField field : fields) {
            field.setText("");
            field.setBackground(Color.WHITE);
        }
    }

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
                    errors.add("Field " + labels[i].getText() + " is mandatory and has to be number.");
                } else if (inputVerifier instanceof RequiredInputVerifier) {
                    errors.add("Field " + labels[i].getText() + " is mandatory.");
                }
            }
        }

        return errors;
    }


    private DataSource getDataSource() {
        return DataSourceProvider.getDataSource();
    }

    private void initManagers() {
        personManager = new PersonManagerImpl(getDataSource());
        System.out.println(personManager.findAllPeople());
    }
    
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
