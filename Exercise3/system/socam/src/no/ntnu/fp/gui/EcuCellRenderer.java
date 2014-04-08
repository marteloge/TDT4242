package no.ntnu.fp.gui;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

import no.ntnu.fp.model.Ecu;

public class EcuCellRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = 1L;
    public EcuCellRenderer()
    {
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Ecu e = (Ecu)value;
        // the default method always returns a JLabel,
        // in fact the superclass inherits from JLabel
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        // build a string from the various  Ecus properties
        String s = String.valueOf(e.getEcuId());
        String text = (s != null ? s : "???");
        String vID = String.valueOf(e.getSwId());
        text += " (" + vID + ")";
        label.setText(text);

        return label;
    }
}
