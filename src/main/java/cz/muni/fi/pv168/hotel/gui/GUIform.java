package cz.muni.fi.pv168.hotel.gui;

import javax.swing.*;

/**
 * @author Masha Shevchenko
 *         Date: 23.04.14
 */
public class GUIform extends JFrame {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton cancelButton;
    private JButton addButton;
    private JTable table1;
    private JButton deleteButton;
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

    public GUIform() {
        super();

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createUIComponents() {

        final Object[][] values = {
                {"Homer Simpson", "Springfield", "742", false},
                {"Marge Simpson", "Springfield", "999", false}
        };

        table1 = new JTable(PersonTableModelFactory.getTableModel(values));

    }
}
