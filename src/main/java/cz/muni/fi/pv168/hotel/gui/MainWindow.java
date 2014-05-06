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
 * @author Masha Shevchenko
 *         Date: 23.04.14
 */
public class MainWindow extends JFrame {

    private static final int CHECK_COLUMN_INDEX = 3;
    private PersonManager personManager;

    private JPanel panel1;
    private JTable table1;
    private JTabbedPane tabbedPane2;
    private JTextField textField4;
    private JTextField textField5;
    private JButton addButton1;
    private JButton cancelButton1;
    private JTextField textField6;
    private JButton submitButton;
    private JButton cancelButton2;
    private JTable table2;
    private JTextField textField7;
    private JButton submitButton1;
    private JButton cancelButton3;
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JButton deleteButton1;
    private JButton cancelButton4;
    private JTabbedPane tabbedPane3;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JButton addButton2;
    private JButton cancelButton5;
    private JTextField textField13;
    private JButton submitButton2;
    private JButton cancelButton6;
    private JTextField textField14;
    private JButton submitButton3;
    private JButton cancelButton7;
    private JTextField textField15;
    private JButton submitButton4;
    private JButton cancelButton8;
    private JTable table3;
    private JTextField textField16;
    private JButton submitButton5;
    private JButton cancelButton9;
    private JButton deleteButton2;
    private JButton cancelButton10;
    private JButton personCancelButton;
    private JButton personAddButton;
    private JTextField personNameField;
    private JTextField personAddressField;
    private JTextField personPhoneField;
    private JButton personDeleteButton;
    private JTable personTable;
    private JLabel personNameLabel;
    private JLabel personAddressLabel;
    private JLabel personPhoneLabel;
    private JTabbedPane personPanel;

    public MainWindow() {
        super();

        // Initialize system
        initManagers();

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        personNameField.setInputVerifier(new RequiredInputVerifier());
        personAddressField.setInputVerifier(new RequiredInputVerifier());
        personPhoneField.setInputVerifier(new NumericRequiredInputVerifier());


        // TODO swing worker
        final List<Person> people = personManager.findAllPeople();

        personAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = validateFields(
                        new JTextField[]{personNameField, personPhoneField, personAddressField},
                        new JLabel[]{personNameLabel, personPhoneLabel, personAddressLabel}
                );

                if (!errors.isEmpty()) {
                    JOptionPane.showMessageDialog(MainWindow.this, StringUtils.printDelimited(errors, "\n"));
                } else {

                    String name = personNameField.getText();
                    String phone = personPhoneField.getText();
                    String address = personAddressField.getText();

                    // TODO swing worker
                    personManager.createPerson(new Person(null, name, phone, address));

                    clearFields(personNameField, personPhoneField, personAddressField);
                    JOptionPane.showMessageDialog(MainWindow.this, "Person " + name + " successfuly created");
                    reloadPersonTable();
                }
            }
        });

        personDeleteButton.addActionListener(new ActionListener() {
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
                    JOptionPane.showMessageDialog(MainWindow.this, "You have to check some people first");
                    return;
                }

                int chosenOption = JOptionPane.showConfirmDialog(MainWindow.this, "Do you really want to remove all selected people (" + toDelete.size() + ")?", null, JOptionPane.YES_NO_OPTION);
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
    }
}
