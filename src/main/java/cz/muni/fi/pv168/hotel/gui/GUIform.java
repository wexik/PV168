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
