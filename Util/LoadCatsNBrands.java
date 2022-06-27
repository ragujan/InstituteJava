/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import model.MySql;

/**
 *
 * @author acer
 */
public class LoadCatsNBrands {

    public void loadCats(JComboBox<String> jcombobox) {

        DefaultComboBoxModel dcmb = (DefaultComboBoxModel) jcombobox.getModel();
        jcombobox.removeAllItems();
        dcmb.setSelectedItem("Select Category");
        try {
            ResultSet rs = MySql.sq("SELECT * FROM `category`");

            while (rs.next()) {

                String utn = rs.getString("category_name");

                jcombobox.addItem(utn);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBrands(JComboBox<String> jcombobox) {

        DefaultComboBoxModel dcmb = (DefaultComboBoxModel) jcombobox.getModel();
        jcombobox.removeAllItems();
        dcmb.setSelectedItem("Select Brand");
        try {
            ResultSet rs = MySql.sq("SELECT * FROM `brand`");
            while (rs.next()) {
                Vector<String> v = new Vector<String>();
                v.add(rs.getString("brand_id"));
                v.add(rs.getString("brand_name"));
                String utn = rs.getString("brand_name");

                jcombobox.addItem(utn);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCities(JComboBox<String> jcombobox) {

        DefaultComboBoxModel dcmb = (DefaultComboBoxModel) jcombobox.getModel();
        jcombobox.removeAllItems();
        dcmb.setSelectedItem("Select City");
        try {
            ResultSet rs = MySql.sq("SELECT * FROM `city`");

            while (rs.next()) {
                String utn = rs.getString("city_name");
                jcombobox.addItem(utn);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSubject(JComboBox<String> jcombobox) {

        DefaultComboBoxModel dcmb = (DefaultComboBoxModel) jcombobox.getModel();
        jcombobox.removeAllItems();
        dcmb.setSelectedItem("Select Subject");
        try {
            ResultSet rs = MySql.sq("SELECT * FROM `subject`");

            while (rs.next()) {
                String utn = rs.getString("subject_name");
                jcombobox.addItem(utn);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadPayments(JComboBox<String> jcombobox) {

        DefaultComboBoxModel dcmb = (DefaultComboBoxModel) jcombobox.getModel();
        jcombobox.removeAllItems();
        dcmb.setSelectedItem("Select Payment");
        try {
            ResultSet rs = MySql.sq("SELECT * FROM `payment_method`");

            while (rs.next()) {
                String utn = rs.getString("payment_method_name");
                jcombobox.addItem(utn);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
