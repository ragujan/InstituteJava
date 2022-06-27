/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MySql;

/**
 *
 * @author acer
 */
public class InsertTable {

    public InsertTable(String tableName, ArrayList<String> columnValueStringArray) {
        this.tableName = tableName;
        this.columnValueStringArray = columnValueStringArray;
        this.columnNameStringArray = new ArrayList<String>();
        this.querybuild = new StringBuilder("");
        setColumnQuery(tableName);
        this.getColumnNames();
        this.setInsertQueryTable();
        this.setValuesforColumns();
        this.executeInsertQuery();
    }

    public InsertTable(String tableName, ArrayList<String> columnValueStringArray, int switcher) {
        if (switcher == 0) {
            this.tableName = tableName;
            this.columnValueStringArray = columnValueStringArray;
            this.columnNameStringArray = new ArrayList<String>();
            this.querybuild = new StringBuilder("");
            setColumnQuery(tableName);
            this.getColumnNames();
            this.setInsertQueryTable();
            this.setValuesforColumns();
            System.out.println("query build is " + this.querybuild);
        } else if (switcher == 1) {

            //(String tableName, ArrayList<String> columnValueStringArray);
        }
    }
    public String query;
    private String tableName;
    private String columnQuery;
    private String columnName;
    public StringBuilder querybuild;
    private ArrayList<String> columnNameStringArray;
    private ArrayList<String> columnValueStringArray;

    private void setColumnQuery(String tableName) {
        this.columnQuery = "SELECT *\n"
                + "FROM INFORMATION_SCHEMA.COLUMNS\n"
                + "WHERE TABLE_SCHEMA = 'institute' AND TABLE_NAME = '" + tableName + "' AND EXTRA !='auto_increment' ORDER BY COLUMN_NAME ASC";
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private ResultSet getColumnNames() {

        ResultSet columnNameRs = null;

        try {
            columnNameRs = MySql.sq(columnQuery);

            while (columnNameRs.next()) {
                columnNameStringArray.add(columnNameRs.getString("COLUMN_NAME"));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InsertTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InsertTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        return columnNameRs;
    }

    private void setInsertQueryTable() {

        querybuild.append("INSERT INTO " + this.tableName + " (");
        for (int i = 0; i < this.columnNameStringArray.size(); i++) {
            querybuild.append(this.columnNameStringArray.get(i));

            if (i != this.columnNameStringArray.size() - 1) {

                querybuild.append(",");
            }
        }
        querybuild.append(") VALUES (");

    }

    private void setValuesforColumns() {

        for (int i = 0; i < columnValueStringArray.size(); i++) {
            querybuild.append(" '" + this.columnValueStringArray.get(i) + "' ");

            if (i != this.columnValueStringArray.size() - 1) {
                querybuild.append(",");
            }
        }
        querybuild.append(");");

    }

    private void executeInsertQuery() {
        try {
            MySql.iud(this.querybuild.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
