/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import Util.CreateObject;
import Util.FilterDocRagRegex;
import Util.LoadCatsNBrands;
import Util.LoadTables;
import Util.SetEmptyItems;
import com.formdev.flatlaf.IntelliJTheme;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.MySql;

/**
 *
 * @author acer
 */
public class AdvancedSearch extends javax.swing.JFrame {

    /**
     * Creates new form AdvancedSeach
     */
    public AdvancedSearch() {
        initComponents();
        loadTable();
        setValidation();
        this.thisad = this;
        this.lcnb = new LoadCatsNBrands();
        this.lcnb.loadSubject(searchSubject);
        tableListernRag();
    }
    AdvancedSearch thisad;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    LoadCatsNBrands lcnb;
    String[] colnames = {"unique_id", "payment_method_name", "paid_amount", "balance", "student_id", "student_name", "class_id", "class_desc", "paymenttime", "duration"};
    String loadTableQuery = "SELECT * FROM `payment`\n"
            + "INNER JOIN `student_has_class`\n"
            + "ON `student_has_class`.`student_has_class_id`=`payment`.`student_has_class_id`\n"
            + "INNER JOIN `class`\n"
            + "ON `class`.`class_id` = `student_has_class`.`class_class_id` \n"
            + "INNER JOIN `teacher`\n"
            + "ON `teacher`.`teacher_id` = `class`.`teacher_id` \n"
            + "INNER JOIN `student`\n"
            + "ON `student`.`student_id` = `student_has_class`.`student_student_id`\n"
            + "INNER JOIN `payment_method`\n"
            + "ON `payment_method`.`payment_method_id` = `payment`.`payment_method_id` \n"
            + "INNER JOIN subject\n"
            + "ON `subject`.`subject_id` = `class`.`subject_id` \n";
    double totalBalance = 0;
    double subjectPrice = 0;
    double newBalance = 0;
    String sname = null;
    String cdesc = null;
    String subname = null;
    String globalsid;
    String globalcid;

    public void setValidation() {

        String priceRegex = "^\\d*([,]\\d*)*([.]\\d*)?";
        FilterDocRagRegex fromprice = new FilterDocRagRegex(fromPrice, priceRegex);
        FilterDocRagRegex toprice = new FilterDocRagRegex(toPrice, priceRegex);
        FilterDocRagRegex fromDuePrice = new FilterDocRagRegex(fromDue, priceRegex);
        FilterDocRagRegex toDuePrice = new FilterDocRagRegex(toDue, priceRegex);

    }

    public void loadTable() {
        String sort = "ORDER BY `paymenttime` ASC";

        StringBuilder stringquerybuild = new StringBuilder();
        stringquerybuild.append(this.loadTableQuery).toString();
        stringquerybuild.append(sort).toString();
        String query = stringquerybuild.toString();

        LoadTables lt = new LoadTables(jTable1, query, colnames);

    }

    public void resetSubject() {
        this.lcnb.loadSubject(searchSubject);
    }

    public void tableListernRag() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {

                    globalsid = jTable1.getValueAt(row, 4).toString();
                    globalcid = jTable1.getValueAt(row, 6).toString();

                    try {
                        ResultSet rs = MySql.sq("SELECT * FROM `payment`\n"
                                + "INNER JOIN `student_has_class`\n"
                                + "ON `student_has_class`.`student_has_class_id`=`payment`.`student_has_class_id`\n"
                                + "INNER JOIN `class`\n"
                                + "ON `class`.`class_id` = `student_has_class`.`class_class_id`\n"
                                + "INNER JOIN `student`\n"
                                + "ON `student`.`student_id` = `student_has_class`.`student_student_id`\n"
                                + "INNER JOIN `payment_method`\n"
                                + "ON `payment_method`.`payment_method_id` = `payment`.`payment_method_id`\n"
                                + "INNER JOIN subject\n"
                                + "ON `subject`.`subject_id` = `class`.`subject_id`\n"
                                + "WHERE `student`.`student_id`='" + globalsid + "' AND `class`.`class_id`='" + globalcid + "'\n"
                                + "ORDER BY `balance` DESC");
                        while (rs.next()) {
                            double balance = Double.parseDouble(rs.getString("balance"));
                            subjectPrice = Double.parseDouble(rs.getString("subject_price"));
                            sname = rs.getString("student_name");
                            cdesc = rs.getString("class_desc");
                            subname = rs.getString("subject_name");
                            totalBalance = balance;
                        }
                        jLabel21.setText(Double.toString(totalBalance));
                        jLabel11.setText(globalcid);
                        jLabel15.setText(globalsid);
                        jLabel17.setText(sname);
                        jLabel13.setText(cdesc);
                        jLabel19.setText(Double.toString(subjectPrice));

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(AdvancedSearch.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdvancedSearch.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        });

    }

    public void advancedSearch() {
        String sort = jComboBox2.getSelectedItem().toString();
        String searchText = searchdesc.getText();
        String teacherText = searchtname.getText();
        String studentText = searchStudentText.getText();
        String subjectText = searchSubject.getSelectedItem().toString();
        String hour = fromDura.getSelectedItem().toString();
        String min = toDura.getSelectedItem().toString();
        String prangeTo = toPrice.getText();
        String prangeFrom = fromPrice.getText();
        String balanceDueFrom = fromDue.getText();
        String balanceDueTo = toDue.getText();
        String todateString = null;
        String fromdateString = null;
        String sortQuery = null;
        boolean isDurationChosen = false;
        boolean isDateInvolved = false;
        boolean isPriceRangeInvolved = false;
        boolean isBalanceDueInvolved = false;
        boolean isStudentInvolved = false;
        if (!prangeTo.isEmpty() && !prangeFrom.isEmpty()) {
            isPriceRangeInvolved = true;
        }
        if (toDate.getDate() != null && fromDate.getDate() != null) {
            isDateInvolved = true;
            todateString = sdf.format(toDate.getDate());
            fromdateString = sdf.format(fromDate.getDate());
        }
        if (!hour.equals("none") && !min.equals("none")) {
            isDurationChosen = true;
        }
        if (!balanceDueTo.isEmpty() && !balanceDueFrom.isEmpty()) {
            isBalanceDueInvolved = true;
        }
        if (!studentText.isEmpty()) {
            isStudentInvolved = true;
        }
        if (searchText.isEmpty() && teacherText.isEmpty()
                && subjectText.equals("Select Subject") && !isDurationChosen
                && !isBalanceDueInvolved && !isDateInvolved && !isPriceRangeInvolved
                && !isStudentInvolved) {
            System.out.println("Technically empty search");
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `student`.`student_name` ASC";
            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `student`.`student_name` DESC";
            } else if (sort.equals("DATETIME NEW TO OLD")) {
                sortQuery = "ORDER BY `paymenttime` DESC";
            } else if (sort.equals("DATETIME OLD TO NEW")) {
                sortQuery = "ORDER BY `paymenttime` ASC";
            } else if (sort.equals("BALANCE ONLY")) {
                sortQuery = "WHERE `payment`.`balance`>'0'";
            } else if (sort.equals("OVERPAID")) {
                sortQuery = "WHERE `payment`.`balance`<'0'";
            } else if (sort.equals("CLEAR PAYMENTS")) {
                sortQuery = "WHERE `payment`.`balance`='0'";
            } else if (sort.equals("PAYMENT HIGH TO LOW")) {
                sortQuery = "ORDER BY `payment`.`paid_amount` DESC";
            } else if (sort.equals("PAYMENT LOW TO HIGH")) {
                sortQuery = "ORDER BY `payment`.`paid_amount` ASC";
            }

            StringBuilder stringquerybuild = new StringBuilder();
            stringquerybuild.append(this.loadTableQuery).toString();
            stringquerybuild.append(sortQuery).toString();
            String query = stringquerybuild.toString();

            LoadTables lt = new LoadTables(jTable1, query, colnames);
        } else {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `student`.`student_name` ASC";
            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `student`.`student_name` DESC";
            } else if (sort.equals("DATETIME NEW TO OLD")) {
                sortQuery = "ORDER BY `paymenttime` DESC";
            } else if (sort.equals("DATETIME OLD TO NEW")) {
                sortQuery = "ORDER BY `paymenttime` ASC";
            } else if (sort.equals("BALANCE ONLY")) {
                sortQuery = "WHERE `payment`.`balance`>'0'";
            } else if (sort.equals("OVERPAID")) {
                sortQuery = "WHERE `payment`.`balance`<'0'";
            } else if (sort.equals("CLEAR PAYMENTS")) {
                sortQuery = "WHERE `payment`.`balance`='0'";
            } else if (sort.equals("PAYMENT HIGH TO LOW")) {
                sortQuery = "ORDER BY `payment`.`paid_amount` DESC";
            } else if (sort.equals("PAYMENT LOW TO HIGH")) {
                sortQuery = "ORDER BY `payment`.`paid_amount` ASC";
            }
            StringBuilder stringquerybuild = new StringBuilder();
            StringBuilder whereQueryBuilder = new StringBuilder();
            Vector<String> v = new Vector<String>();
            boolean queriesInvolved = false;
            if (!searchText.isEmpty()) {
                String descWhere = "`class_desc` LIKE '%" + searchText + "%' ";
                v.add(descWhere);
                queriesInvolved = true;
            }
            if (!teacherText.isEmpty()) {
                String teacherWhere = "`teacher_name` LIKE '%" + teacherText + "%' ";
                v.add(teacherWhere);
                queriesInvolved = true;
            }
            if (!subjectText.isEmpty() && !(subjectText.equals("Select Subject"))) {
                String subjectWhere = "`subject_name` = '" + subjectText + "'  ";
                v.add(subjectWhere);
                queriesInvolved = true;
            }
            if (!hour.equals("none") && !min.equals("none")) {
                String hours = hour + ":" + min + ":" + "00";
                String timeWhere = "`duration`='" + hours + "' ";
                v.add(timeWhere);
                queriesInvolved = true;
            }
            if (isDateInvolved) {
                String fd = fromdateString + " 00:00:00";
                String td = todateString + " 00:00:00";
                String dateWhere = "(`class_time` BETWEEN '" + fd + "' AND '" + td + "')  ";
                v.add(dateWhere);
                queriesInvolved = true;
            }
            if (isPriceRangeInvolved) {
                String fp = prangeFrom;
                String tp = prangeTo;
                String priceRangeWhere = "(`subject`.`subject_price` BETWEEN '" + fp + "' AND '" + tp + "' )";
                v.add(priceRangeWhere);
                queriesInvolved = true;
            }
            if (isStudentInvolved) {
                String studentQuery = "`student`.`student_name`='" + studentText + "' ";
                v.add(studentQuery);
                queriesInvolved = true;
            }
            if (isBalanceDueInvolved) {
                String balancedueQuery = "(`payment`.`balance` BETWEEN '" + balanceDueFrom + "' AND '" + balanceDueTo + "') ";
                v.add(balancedueQuery);
                queriesInvolved = true;
            }
            System.out.println("vector size is " + v.size());
            if (queriesInvolved) {
                whereQueryBuilder.append("where ");
            }

            for (int i = 0; i < v.size(); i++) {
                System.out.println("vectors are " + v.get(i));

                whereQueryBuilder.append("");
                whereQueryBuilder.append(v.get(i));

                if (i != v.size() - 1) {
                    whereQueryBuilder.append("AND ");
                }
            }
            System.out.println("string build is " + stringquerybuild);
            stringquerybuild.append(this.loadTableQuery);
            stringquerybuild.append(whereQueryBuilder);
            stringquerybuild.append(sortQuery);
            String query = stringquerybuild.toString();
            System.out.println("where query is " + whereQueryBuilder);
            LoadTables lt = new LoadTables(jTable1, query, this.colnames);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox<>();
        Search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        searchdesc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        searchtname = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        searchSubject = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        fromDura = new javax.swing.JComboBox<>();
        toDura = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        searchStudentText = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fromDate = new com.toedter.calendar.JDateChooser();
        toDate = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        fromPrice = new javax.swing.JTextField();
        toPrice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        fromDue = new javax.swing.JTextField();
        toDue = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payt_Id", "Payt", "Amt", "Bal", "SID", "SNAME", "CID", "Cl_DESC", "Time", "Dura"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(6).setMaxWidth(60);
        }

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NAME AZ", "NAME ZA", "DATETIME NEW TO OLD", "DATETIME OLD TO NEW", "BALANCE ONLY", "OVERPAID", "CLEAR PAYMENTS", "PAYMENT HIGH TO LOW", "PAYMENT LOW TO HIGH" }));

        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        jLabel1.setText("Sort Options");

        jLabel4.setText("Desc");

        jLabel7.setText("Teacher");

        searchSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSubjectActionPerformed(evt);
            }
        });
        searchSubject.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                searchSubjectPropertyChange(evt);
            }
        });

        jLabel8.setText("Subject");

        fromDura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "1", "2", "3", "4", "5", "6", "7", "8" }));

        toDura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "00", "15", "30", "45" }));

        jLabel9.setText("Duration");

        jButton1.setText("Select Teacher");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Select Student");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Student");

        jLabel3.setText("Date BetWeen");

        jLabel5.setText("Price Range");

        fromPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromPriceActionPerformed(evt);
            }
        });

        jLabel6.setText("Due Between");

        jButton3.setText("Reset Subj");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Select Class");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(fromDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchdesc, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(searchtname, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(searchSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(toDate, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromPrice)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(searchStudentText)
                    .addComponent(toPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fromDue, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toDue, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fromDura, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toDura, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel2)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(toDura)
                    .addComponent(fromDura)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchdesc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchtname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchStudentText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(toDate, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(fromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromPrice)
                    .addComponent(toPrice)
                    .addComponent(fromDue)
                    .addComponent(toDue))
                .addContainerGap())
        );

        jButton4.setText("Home");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("Clear");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel10.setText("ClassID");

        jLabel11.setText("none");

        jLabel12.setText("Class Desc");

        jLabel13.setText("none");

        jLabel17.setText("none");

        jLabel16.setText("Student Name");

        jLabel15.setText("none");

        jLabel14.setText("StudentID");

        jLabel18.setText("Subject Payment ");

        jLabel19.setText("none");

        jLabel20.setText("Total Balance");

        jLabel21.setText("none");

        jButton7.setText("Make Payment");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel15)))
                .addGap(42, 42, 42)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel16))
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 384, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(Search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(9, 9, 9)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
        this.advancedSearch();

    }//GEN-LAST:event_SearchActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(new TeacherReg(this));
      
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        StudentReg sr = new StudentReg(this);
        sr.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.lcnb.loadSubject(searchSubject);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(new Home());
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(new ClassReg(this));

    }//GEN-LAST:event_jButton5ActionPerformed

    private void searchSubjectPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_searchSubjectPropertyChange
        // TODO add your handling code here:


    }//GEN-LAST:event_searchSubjectPropertyChange

    private void searchSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSubjectActionPerformed
        // TODO add your handling code here:
        //  searchdesc.setText("");
    }//GEN-LAST:event_searchSubjectActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        JComponent[] jp = {searchdesc, searchStudentText, searchtname,
            fromDate, toDate, fromPrice, toPrice, fromDue, toDue, fromDura, toDura};
        SetEmptyItems.emptyItems(jp);
        lcnb.loadSubject(searchSubject);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String[] data = {globalsid, sname,
            globalcid, cdesc, subname, Double.toString(totalBalance), Double.toString(subjectPrice)};
        MakePayment mp = new MakePayment(thisad, data);
        mp.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void fromPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fromPriceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdvancedSearch().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Search;
    private com.toedter.calendar.JDateChooser fromDate;
    private javax.swing.JTextField fromDue;
    private javax.swing.JComboBox<String> fromDura;
    private javax.swing.JTextField fromPrice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextField searchStudentText;
    public javax.swing.JComboBox<String> searchSubject;
    public javax.swing.JTextField searchdesc;
    public javax.swing.JTextField searchtname;
    private com.toedter.calendar.JDateChooser toDate;
    private javax.swing.JTextField toDue;
    private javax.swing.JComboBox<String> toDura;
    private javax.swing.JTextField toPrice;
    // End of variables declaration//GEN-END:variables
}
