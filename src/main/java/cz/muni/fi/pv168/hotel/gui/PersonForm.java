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
    private Long currentlyUpdatedPersonId = null;
    
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
        initVerifiers();


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
                if (currentlyUpdatedPersonId != null) {
                    new LoadPersonWorker(currentlyUpdatedPersonId).execute();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Person> checkedPeople = getCheckedPeople();

                if (checkedPeople.isEmpty()) {
                    JOptionPane.showMessageDialog(PersonForm.this, "You have to select a person which to update");
                } else if (checkedPeople.size() > 1) {
                    JOptionPane.showMessageDialog(PersonForm.this, "Please select only one person for update.");
                } else {
                    setFieldValues(checkedPeople.get(0));
                }
            }
        });

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

                    Person person = assemblePersonFromInput();
                    String message;
                    if (person.getId() == null) {
                        new CreatePersonWorker(person).execute();
                        message = "Person " + person.getName() + " successfuly created";
                    } else {
                        new UpdateActionPersonWorker(person).execute();
                        message = "Person " + person.getName() + " successfuly updated";
                    }

                    JOptionPane.showMessageDialog(PersonForm.this, message);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Person> checkedPeople = getCheckedPeople();

                if (checkedPeople.isEmpty()) {
                    JOptionPane.showMessageDialog(PersonForm.this, "You have to check some people first");
                    return;
                }

                int chosenOption = JOptionPane.showConfirmDialog(PersonForm.this, "Do you really want to remove all selected people (" + checkedPeople.size() + ")?", null, JOptionPane.YES_NO_OPTION);
                if (chosenOption == JOptionPane.YES_OPTION) {
                    new DeleteActionPersonWorker(checkedPeople).execute();
                }
            }
        });


        reloadPersonTable();
    }

    private List<Person> getCheckedPeople() {
        int rowCount = personTable.getModel().getRowCount();
        List<Person> checkedPeople = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Boolean isChecked = (Boolean) personTable.getModel().getValueAt(i, CHECK_COLUMN_INDEX);

            if (isChecked) {
                Person person = (Person) personTable.getModel().getValueAt(i, -1);
                checkedPeople.add(person);
            }
        }
        return checkedPeople;
    }

    private Person assemblePersonFromInput() {
        Long id = currentlyUpdatedPersonId;
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        return new Person(id, name, phone, address);
    }

    private DataSource getDataSource() {
        return DataSourceProvider.getDataSource();
    }

    private void initManagers() {
        personManager = new PersonManagerImpl(getDataSource());
    }

    private void initVerifiers() {
        nameField.setInputVerifier(new RequiredInputVerifier());
        addressField.setInputVerifier(new RequiredInputVerifier());
        phoneField.setInputVerifier(new NumericRequiredInputVerifier());
    }

    private void setFieldValues(Person person) {
        nameField.setText(person.getName());
        addressField.setText(person.getAddress());
        phoneField.setText(person.getPhoneNumber());
        currentlyUpdatedPersonId = person.getId();
    }

    private void clearAllFields() {
        clearFields(nameField, phoneField, addressField);
    }

    private void clearFields(JTextField ... fields) {
        for (JTextField field : fields) {
            field.setText("");
            field.setBackground(Color.WHITE);
        }
    }

    private void reloadPersonTable() {
        new LoadAllPersonWorker().execute();
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
                    errors.add("Field " + labels[i].getText() + " is mandatory and has to be number.");
                } else if (inputVerifier instanceof RequiredInputVerifier) {
                    errors.add("Field " + labels[i].getText() + " is mandatory.");
                }
            }
        }

        return errors;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    /**
     * Abstract swing worker for doing create/update/delete action method and refreshing
     * person table and input fields
     *
     * @param <T> type of input parameter of action method
     */
    private abstract class ActionPersonWorker<T> extends SafeSwingWorker<Void, Void> {

        private T input;

        private ActionPersonWorker(T input) {
            this.input = input;
        }

        protected T getInput() {
            return input;
        }

        @Override
        protected void done() {
            currentlyUpdatedPersonId = null;
            clearAllFields();
            reloadPersonTable();
        }
    }

    /**
     * Swing worker for creating person
     */
    private class CreatePersonWorker extends ActionPersonWorker<Person> {

        private CreatePersonWorker(Person person) {
            super(person);
        }

        @Override
        protected Void doInBackground() throws Exception {
            personManager.createPerson(getInput());
            return null;
        }
    }

    /**
     * Swing worker for updating person
     */
    private class UpdateActionPersonWorker extends ActionPersonWorker<Person> {

        private UpdateActionPersonWorker(Person person) {
            super(person);
        }

        @Override
        protected Void doInBackground() throws Exception {
            personManager.updatePerson(getInput());
            return null;
        }
    }

    /**
     * Swing worker for deleting person
     */
    private class DeleteActionPersonWorker extends ActionPersonWorker<List<Person>> {

        private DeleteActionPersonWorker(List<Person> people) {
            super(people);
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Person person : getInput()) {
                personManager.deletePerson(person);
            }
            return null;
        }
    }

    /**
     * Swing worker for loading all people
     */
    private class LoadAllPersonWorker extends SafeSwingWorker<List<Person>, Void> {

        @Override
        protected List<Person> doInBackground() throws Exception {
            return personManager.findAllPeople();
        }

        @Override
        protected void done() {
            personTable.setModel(new PersonTableModel(safeGet()));
            personTable.repaint();
        }
    }

    /**
     * Swing worker for loading person by ID
     */
    private class LoadPersonWorker extends SafeSwingWorker<Person, Void> {

        private Long personId;

        private LoadPersonWorker(Long personId) {
            this.personId = personId;
        }

        @Override
        protected Person doInBackground() throws Exception {
            return personManager.findPersonById(personId);
        }

        @Override
        protected void done() {
            setFieldValues(safeGet());
        }
    }
}
