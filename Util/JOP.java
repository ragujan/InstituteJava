/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class JOP {

    public static void setJOPMessage(JFrame jf, String message, String title, int type) {
        if (type == 1) {
            JOptionPane.showMessageDialog(jf, message, title, JOptionPane.WARNING_MESSAGE);
        }
        if (type == 2) {
            JOptionPane.showMessageDialog(jf, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
