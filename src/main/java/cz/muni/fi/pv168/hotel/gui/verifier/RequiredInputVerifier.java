package cz.muni.fi.pv168.hotel.gui.verifier;

import cz.muni.fi.pv168.hotel.util.StringUtils;

import javax.swing.*;
import java.awt.*;


/**
 * @author mashka
 */
public class RequiredInputVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        return !StringUtils.isEmptyOrNull(text);
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        JTextField field = (JTextField) input;

        if (!valid) {
            // Red color with 50 % transparency
            field.setBackground(new Color(255,0,0,50));
        } else {
            field.setBackground(Color.WHITE);
        }

        // allow to change focus always
        return true;
    }
}
