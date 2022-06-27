/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;

/**
 *
 * @author User
 */
public class MySql {

    private static final String DBNAME = "institute";
    private static final String PASSWORD = "ragJN100Mania";
    private static final String USERNAME = "root";
    private static final String PORT = "8080";
    private static final String HOST = "localhost";
    private static Connection con;

    public static Statement con() throws ClassNotFoundException, SQLException {
        if (con == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBNAME, USERNAME, PASSWORD);
        }
        return con.createStatement();
    }

    public static void iud(String query) {
        try {
            con().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet sq(String query) throws ClassNotFoundException, SQLException {
        ResultSet rs = MySql.con().executeQuery(query);
        return rs;
    }

    public static Connection getConnection() {
        try {
            if (con == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBNAME, USERNAME, PASSWORD);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return con;
    }
}
