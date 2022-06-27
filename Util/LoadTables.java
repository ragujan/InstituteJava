/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.MySql;

/**
 *
 * @author acer
 */
public class LoadTables {

    public String query;
    public String[] colnames;
    public JTable jt;
    int jtableColCount;

    public LoadTables() {
    }

    public LoadTables(JTable jt, String query,String[] colnames) {

        loadTable(jt, query,colnames);
    }

    public void loadTable(JTable jt, String query, String[] colnames) {

        jtableColCount = jt.getColumnCount();

        DefaultTableModel dftm = (DefaultTableModel) jt.getModel();
        dftm.setRowCount(0);
        try {
            ResultSet rs = MySql.sq(query);
            while (rs.next()) {
                Vector<String> v = new Vector<String>();
                for (int i = 0; i < jtableColCount; i++) {
                    v.add(rs.getString(colnames[i]));
                }
                dftm.addRow(v);
            }
            jt.setModel(dftm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTable(JTable jt) {

        jtableColCount = jt.getColumnCount();

        DefaultTableModel dftm = (DefaultTableModel) jt.getModel();
        dftm.setRowCount(0);
        try {
            ResultSet rs = MySql.sq(this.query);
            while (rs.next()) {
                Vector<String> v = new Vector<String>();
                for (int i = 0; i < jtableColCount; i++) {
                    v.add(rs.getString(colnames[i]));
                }
                dftm.addRow(v);
            }
            jt.setModel(dftm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
