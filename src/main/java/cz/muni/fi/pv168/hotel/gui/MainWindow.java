package cz.muni.fi.pv168.hotel.gui;

import cz.muni.fi.pv168.hotel.PersonManager;

import javax.swing.*;

/**
 * @author Masha Shevchenko
 *         Date: 23.04.14
 */
public class MainWindow extends JFrame {

    private static final int CHECK_COLUMN_INDEX = 3;
    private PersonManager personManager;

    private JPanel panel1;
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
    private JTabbedPane personPanel;

    public MainWindow() {
        super();

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createUIComponents() {
    }
}
