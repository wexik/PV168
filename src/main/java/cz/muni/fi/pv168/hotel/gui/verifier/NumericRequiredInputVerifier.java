package cz.muni.fi.pv168.hotel.gui.verifier;

import cz.muni.fi.pv168.hotel.util.StringUtils;

import javax.swing.*;

/**
 * @author kurocenko
 */
public class NumericRequiredInputVerifier extends RequiredInputVerifier {

    @Override
    public boolean verify(JComponent input) {
        // Verify whether is not empty
        boolean verify = super.verify(input);

        JTextField field = (JTextField) input;

        return StringUtils.isNumber(field.getText());
    }
}
