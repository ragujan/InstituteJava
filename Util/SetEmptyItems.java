/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author acer
 */
class EmptyDocumentFilter {

    JTextField jb;

    public EmptyDocumentFilter(JTextField jb) {
        this.jb = jb;
    }
    AbstractDocument ab = (AbstractDocument) jb.getDocument();

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {
        String jtText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = jtText.substring(0, offset) + text;

        fb.remove(0, offset);

    }
}

public class SetEmptyItems {

    public static void emptyItems(JComponent[] jcp) {
        for (int i = 0; i < jcp.length; i++) {

            if (jcp[i] instanceof JLabel) {
                JLabel jb = (JLabel) jcp[i];
                jb.setText("none");
            }

            if (jcp[i] instanceof JTextField) {
                JTextField jb = (JTextField) jcp[i];

                jb.setText("");
            }

            if (jcp[i] instanceof JComboBox) {
                JComboBox jb = (JComboBox) jcp[i];
                jb.setSelectedIndex(0);
            }
            if (jcp[i] instanceof JDateChooser) {
                JDateChooser jb = (JDateChooser) jcp[i];
                jb.setDate(null);
            }
        }
    }

    public static void setDefaultValues(String value, JComponent jcp) {
        if (jcp instanceof JLabel) {
            JLabel jb = (JLabel) jcp;
            jb.setText(value);
        }

        if (jcp instanceof JTextField) {
            JTextField jb = (JTextField) jcp;

            jb.setText(value);
        }

        if (jcp instanceof JComboBox) {
            JComboBox jb = (JComboBox) jcp;
            jb.setSelectedIndex(0);
        }
        if (jcp instanceof JDateChooser) {
            JDateChooser jb = (JDateChooser) jcp;
            jb.setDate(null);
        }
    }
}
