/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.util.ArrayList;

/**
 *
 * @author acer
 */
public class SearchTable {

    public SearchTable(ArrayList<String> tableNames, String whereQuery) {
        this.tableNames = tableNames;
        this.whereQuery = whereQuery;
        this.tableBuild = new StringBuilder();
        this.setTableQuery();
        this.setWhereQuery();
    }

    public SearchTable(ArrayList<String> tableNames) {
        this.tableNames = tableNames;
        this.whereQuery = whereQuery;
        this.tableBuild = new StringBuilder();
        this.setTableQuery();

    }
    ArrayList<String> tableNames;
    String whereQuery;
    StringBuilder tableBuild;
    StringBuilder globalSb = new StringBuilder();

    public void setTableQuery() {
        globalSb = new StringBuilder();
        globalSb.append("SELECT * FROM `" + this.tableNames.get(0) + "` ");
        if (this.tableNames.size() > 1) {
            for (int i = 1; i < this.tableNames.size(); i++) {

                String tableNamePair = (String) this.tableNames.get(i);
                String[] tableNamePairArray = tableNamePair.split(",");
                StringBuilder localSb = new StringBuilder();

                String connectingColName = "";
                for (int j = 0; j < tableNamePairArray.length; j++) {

                    if (j == 0) {

                        localSb.append("INNER JOIN `" + tableNamePairArray[j] + "` ON `" + tableNamePairArray[j] + "`.`" + tableNamePairArray[j] + "_id` ");
                        connectingColName = "" + tableNamePairArray[j] + "_id";
                    }
                    if (j == tableNamePairArray.length - 1) {
                        localSb.append("= `" + tableNamePairArray[j] + "`.`" + connectingColName + "` ");
                        globalSb.append(localSb);
                    }

                }

            }
        }

    }

    public String getTableQuery() {
        return this.globalSb.toString();
    }

    public void setWhereQuery() {
        tableBuild.append(whereQuery);
        System.out.println(tableBuild);
    }

}
