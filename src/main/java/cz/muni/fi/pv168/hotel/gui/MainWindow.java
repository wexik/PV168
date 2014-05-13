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
    private JTabbedPane tabbedPanel;
    private JTabbedPane roomPanel;

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
