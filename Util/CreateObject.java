/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import com.formdev.flatlaf.IntelliJTheme;
import gui.Home;
import gui.TeacherReg;
import javax.swing.JFrame;

/**
 *
 * @author acer
 */
public class CreateObject {

    public static void make(JFrame jf) {
        try {
            IntelliJTheme.setup(Home.class.getResourceAsStream(
                    "../resources/github_dark.theme.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jf.setVisible(true);
    }

    public static void make(JFrame closing, JFrame jf, boolean isDispose) {
        make(jf);

        if (isDispose) {
            closing.dispose();
        }

    }
}
