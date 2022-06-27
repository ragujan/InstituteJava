/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import Util.BasicValidator;
import Util.CreateObject;
import Util.FilterDocRagRegex;
import Util.InsertTable;
import Util.JOP;
import Util.LoadCatsNBrands;
import Util.LoadTables;
import Util.SearchTable;
import Util.SetEmptyItems;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.MySql;

/**
 *
 * @author acer
 */
public class StudentReg extends javax.swing.JFrame {

    /**
     * Creates new form StudentReg
     */
    public StudentReg() {
        initComponents();
        setValidation();

        LoadCatsNBrands lcnb = new LoadCatsNBrands();
        lcnb.loadCities(jComboBox1);
        loadTable();
        tableListernRag();
        jButton2.setEnabled(false);

    }

    public StudentReg(StudentEnrollment se) {
        this();
        isStudentEnrollInvolved = true;
        this.se = se;
        this.thisStudentReg = this;

    }

    public StudentReg(AdvancedSearch as) {
        this();
        this.as = as;
        isAdvancedSearchInvolved = true;
        this.thisStudentReg = this;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    StudentReg thisStudentReg;
    boolean isStudentEnrollInvolved;
    StudentEnrollment se;
    boolean isAdvancedSearchInvolved;
    AdvancedSearch as;
    String selectedStudentId;
    String whereQuery = "";
    String[] colnames = {"student_id", "student_name", "student_contact", "dob", "address", "city_name"};
    String loadTableQuery = "SELECT \n"
            + "student.student_id,\n"
            + "student.student_name,\n"
            + "student.student_contact,\n"
            + "student.dob,\n"
            + "CONCAT(address.address_line_1,' ',address.address_line_2)  AS `address`,\n"
            + "city.city_name\n"
            + "FROM student \n"
            + "INNER JOIN address\n"
            + "ON address.address_id = student.address_id\n"
            + "INNER JOIN city \n"
            + "ON city.city_id = address.city_id ";

    public void basicSearch(String st, String pn) {
        String sort = jComboBox2.getSelectedItem().toString();
        String searchText = st;
        String pnumber = pn;

        String sortQuery = null;

        if (searchText.isEmpty() && pnumber.isEmpty()) {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `student`.`student_name` ASC";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `student`.`student_name` DESC";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("DOB OLD TO YOUNG")) {
                sortQuery = "ORDER BY `student`.`dob` DESC";
            } else if (sort.equals("DOB YOUNG TO OLD")) {
                sortQuery = "ORDER BY `student`.`dob` ASC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address`.`address_line_1` DESC";
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
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("DOB OLD TO YOUNG")) {
                sortQuery = "ORDER BY `student`.`dob` DESC";
            } else if (sort.equals("DOB YOUNG TO OLD")) {
                sortQuery = "ORDER BY `student`.`dob` ASC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address`.`address_line_1` DESC";
            }
            StringBuilder stringquerybuild = new StringBuilder();

            StringBuilder whereQueryBuilder = new StringBuilder();
            Vector<String> v = new Vector<String>();

            if (!searchText.isEmpty()) {
                String descWhere = "`student_name` LIKE '%" + searchText + "%' ";
                v.add(descWhere);
            }
            if (!pnumber.isEmpty()) {
                String teacherWhere = "`student_contact` LIKE '%" + pnumber + "%' ";
                v.add(teacherWhere);
            }

            System.out.println("vector size is " + v.size());
            whereQueryBuilder.append("where ");
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

            LoadTables lt = new LoadTables(jTable1, query, colnames);
        }

    }

    public void basicSearch() {
        String sort = jComboBox2.getSelectedItem().toString();
        String searchText = jTextField5.getText();
        String sortQuery = null;
        String pnumber = jTextField6.getText();

        if (searchText.isEmpty() && pnumber.isEmpty()) {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `student`.`student_name` ASC";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `student`.`student_name` DESC";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("DOB OLD TO YOUNG")) {
                sortQuery = "ORDER BY `student`.`dob` DESC";
            } else if (sort.equals("DOB YOUNG TO OLD")) {
                sortQuery = "ORDER BY `student`.`dob` ASC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address`.`address_line_1` DESC";
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
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("DOB OLD TO YOUNG")) {
                sortQuery = "ORDER BY `student`.`dob` DESC";
            } else if (sort.equals("DOB YOUNG TO OLD")) {
                sortQuery = "ORDER BY `student`.`dob` ASC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address`.`address_line_1` DESC";
            }
            StringBuilder stringquerybuild = new StringBuilder();

            StringBuilder whereQueryBuilder = new StringBuilder();
            Vector<String> v = new Vector<String>();

            if (!searchText.isEmpty()) {
                String descWhere = "`student_name` LIKE '%" + searchText + "%' ";
                v.add(descWhere);
            }
            if (!pnumber.isEmpty()) {
                String teacherWhere = "`student_contact` LIKE '%" + pnumber + "%' ";
                v.add(teacherWhere);
            }

            System.out.println("vector size is " + v.size());
            whereQueryBuilder.append("where ");
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

            LoadTables lt = new LoadTables(jTable1, query, colnames);
        }
    }

    public void setValidation() {
        String nameregex = "(([A-Z])([a-z])* ([A-Z])([a-z])*|([A-Z])([a-z])* |([A-Z])([a-z])*)";
        FilterDocRagRegex name = new FilterDocRagRegex(jTextField1, nameregex);
        String contactregex = "((([0][7][24-8][0-9]{7})|([0][7][24-8][0-9]*))|([0][7][24-8])|[0][7]|[0])";
        FilterDocRagRegex contact = new FilterDocRagRegex(jTextField3, contactregex);

    }

    public void loadTable() {
        String sort = "ORDER BY `student`.`student_name` ASC";

        StringBuilder stringquerybuild = new StringBuilder();
        stringquerybuild.append(this.loadTableQuery).toString();
        stringquerybuild.append(sort).toString();
        String query = stringquerybuild.toString();
        String[] colnames = {"student_id", "student_name", "student_contact", "dob", "address", "city_name"};
        LoadTables lt = new LoadTables(jTable1, query, colnames);

    }

    public void tableListernRag() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {

                    String sid = jTable1.getValueAt(row, 0).toString();
                    String sname = jTable1.getValueAt(row, 1).toString();
                    String scontact = jTable1.getValueAt(row, 2).toString();
                    String sdob = jTable1.getValueAt(row, 3).toString();
                    String address = jTable1.getValueAt(row, 4).toString();
                    String city = jTable1.getValueAt(row, 5).toString();
                    String[] addressSplit = address.split(" ");
                    String add1 = addressSplit[0];
                    String add2 = addressSplit[1];
                    if (isStudentEnrollInvolved) {
                        se.sid.setText(sid);
                        se.sname.setText(sname);
                        thisStudentReg.dispose();

                    }
                    if (isAdvancedSearchInvolved) {
                        as.searchStudentText.setText(sname);
                        thisStudentReg.dispose();

                    } else {
                        jTextField1.setText(sname);
                        jTextField3.setText(scontact);
                        jComboBox1.setSelectedItem(city);
                        jTextField4.setText(add1);
                        jTextField2.setText(add2);
                        selectedStudentId = sid;
                    }
                    LocalDate date = LocalDate.parse(sdob);
                    try {
                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(sdob);
                        System.out.println(d + "DDD");
                        jDateChooser1.setDate(d);
                    } catch (ParseException ex) {
                        Logger.getLogger(StudentReg.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println(jDateChooser1.getDateFormatString());
                    //  jDateChooser1.setDate(sdf.parse(sdob));
                    //  Date d = new Date
                    //jDateChooser1.setDate()
                    jButton2.setEnabled(true);
                    jButton1.setEnabled(false);
                    jTextField3.setEditable(false);
                    jDateChooser1.setEnabled(false);

                }
            }

        });

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
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(250, 300));

        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel23.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel24.setLayout(new java.awt.BorderLayout());

        jLabel5.setText("Name");
        jPanel24.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel24);

        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel11.setText("Add_1");
        jPanel25.add(jLabel11, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel25);

        jPanel26.setLayout(new java.awt.BorderLayout());

        jLabel10.setText("Add_2");
        jPanel26.add(jLabel10, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel26);

        jPanel27.setLayout(new java.awt.BorderLayout());

        jLabel9.setText("Contact");
        jPanel27.add(jLabel9, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel27);

        jLabel8.setText("DOB");
        jPanel23.add(jLabel8);

        jLabel7.setText("City");
        jPanel23.add(jLabel7);

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel14.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel16.setLayout(new java.awt.BorderLayout());
        jPanel16.add(jTextField1, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel16);

        jPanel18.setLayout(new java.awt.BorderLayout());
        jPanel18.add(jTextField4, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel18);

        jPanel20.setLayout(new java.awt.BorderLayout());
        jPanel20.add(jTextField2, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel20);

        jPanel22.setLayout(new java.awt.BorderLayout());
        jPanel22.add(jTextField3, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel22);

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jPanel14.add(jDateChooser1);

        jPanel14.add(jComboBox1);

        jButton1.setText("Enter");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setText("Home");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGap(16, 16, 16))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S_ID", "S_Name", "S_Contact", "S_DOB", "Address", "City"
            }
        ));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
        );

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NAME AZ", "NAME ZA", "CITY AZ", "CITY ZA", "DOB OLD TO YOUNG", "DOB YOUNG TO OLD", "ADDRESS AZ", "ADDRESS ZA", " " }));

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Home");

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(38, 38, 38)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sdf.format(new Date());
        if (jDateChooser1.getDate() != null) {
            SimpleDateFormat years = new SimpleDateFormat("yyyyMMdd");
            int todayinInt = Integer.parseInt(years.format(new Date()));
            int bdayinInt = Integer.parseInt(years.format(jDateChooser1.getDate()));
            int dateDiff = (todayinInt - bdayinInt) / 10000;
            System.out.println("difference is " + dateDiff);
            if (!(dateDiff < 14)) {
                String selectedDate = sdf.format(jDateChooser1.getDate());
                System.out.println("selected date is " + selectedDate);

            } else {
                jDateChooser1.setDate(null);
                JOP.setJOPMessage(this, "Student Should be atleast age 14", "Warning", 1);
            }

        }

    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String name = jTextField1.getText();
        String contact = jTextField3.getText();
        String contactregex = "([0][7][24-8][0-9]{7})";
        String dob = null;
        if (jDateChooser1.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dob = sdf.format(jDateChooser1.getDate());

        }

        String add1 = jTextField4.getText();
        String add2 = jTextField2.getText();
        String city = jComboBox1.getSelectedItem().toString();
        if (BasicValidator.emptyCheck(name)) {
            JOP.setJOPMessage(this, "Empty Name", "Warning", 1);
        } else if (BasicValidator.emptyCheck(add1)) {
            JOP.setJOPMessage(this, "Empty address 1", "Warning", 1);
        } else if (BasicValidator.emptyCheck(add2)) {
            JOP.setJOPMessage(this, "Empty address 2", "Warning", 1);
        } else if (BasicValidator.emptyCheck(city, "Select City")) {
            JOP.setJOPMessage(this, "Select a valid City", "Warning", 1);
        } else if (!BasicValidator.regexMatcher(contact, contactregex)) {
            JOP.setJOPMessage(this, "contact number is not valid", "Warning", 1);
        } else if (jDateChooser1.getDate() == null) {
            JOP.setJOPMessage(this, "Please choose date of birth", "Warning", 1);
        } else {
            try {
                ResultSet rs = MySql.sq("SELECT * FROM `student` WHERE `student_contact`='" + contact + "'");

                if (rs.next()) {
                    JOP.setJOPMessage(this, "This contact number is already taken", "Warning", 1);
                } else {

                    rs = MySql.sq("SELECT `city_id` FROM `city` WHERE `city_name`='" + city + "'");
                    rs.next();
                    String cityId = rs.getString("city_id");
                    System.out.println("city id is " + cityId);
                    ArrayList<String> info = new ArrayList<String>();
                    info.add(add1);
                    info.add(add2);
                    info.add(cityId);
                    InsertTable it = new InsertTable("address", info);
                    rs = MySql.sq("SELECT `address_id` FROM `address` WHERE "
                            + "`address_line_1`='" + add1 + "' AND  `address_line_2`='" + add2 + "' AND `city_id`='" + cityId + "'");
                    rs.next();
                    String addressId = rs.getString("address_id");
                    info = new ArrayList<String>();
                    info.add(addressId);
                    info.add(dob);
                    info.add(contact);
                    info.add(name);
                    new InsertTable("student", info);
                    JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jDateChooser1};
                    SetEmptyItems.emptyItems(jp);
                    loadTable();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StudentReg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(StudentReg.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String name = jTextField1.getText();
        String contact = jTextField3.getText();
        String contactregex = "([0][7][24-8][0-9]{7})";
        String dob = null;
        if (jDateChooser1.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dob = sdf.format(jDateChooser1.getDate());

        }

        String add1 = jTextField4.getText();
        String add2 = jTextField2.getText();
        String city = jComboBox1.getSelectedItem().toString();
        if (BasicValidator.emptyCheck(name)) {
            JOP.setJOPMessage(this, "Empty Name", "Warning", 1);
        } else if (BasicValidator.emptyCheck(add1)) {
            JOP.setJOPMessage(this, "Empty address 1", "Warning", 1);
        } else if (BasicValidator.emptyCheck(add2)) {
            JOP.setJOPMessage(this, "Empty address 2", "Warning", 1);
        } else if (BasicValidator.emptyCheck(city, "Select City")) {
            JOP.setJOPMessage(this, "Select a valid City", "Warning", 1);
        } else if (!BasicValidator.regexMatcher(contact, contactregex)) {
            JOP.setJOPMessage(this, "contact number is not valid", "Warning", 1);
        } else if (jDateChooser1.getDate() == null) {
            JOP.setJOPMessage(this, "Please choose date of birth", "Warning", 1);
        } else {

            try {
                //search query for getting the city id
                ResultSet rs = MySql.sq("SELECT `city_id` FROM `city` WHERE `city_name`='" + city + "'");
                rs.next();
                //got the city id
                String cityId = rs.getString("city_id");
                //check for address if its already exists
                ResultSet addressCheck = MySql.sq("SELECT * FROM `address` INNER JOIN `city` ON `city`.`city_id`=`address`.`city_id` "
                        + "WHERE `address_line_1`='" + add1 + "' AND `address_line_2`='" + add2 + "' AND `city_name`='" + city + "'  ");
                //if it does exits just give them a warning just update the name
                if (addressCheck.next()) {
                    String alreadyExistingAddressId = addressCheck.getString("address_id");
                    int op = JOptionPane.showConfirmDialog(this, "This address already exists do you want to set it as a your address ", "Duplicate GRN entry", JOptionPane.YES_NO_OPTION);
                    if (op == 0) {
                        MySql.iud("UPDATE `student` SET `student_name`='" + name + "',`address_id`='" + alreadyExistingAddressId + "' WHERE `student_id`='" + selectedStudentId + "'");
                    } else {
                        MySql.iud("UPDATE `student` SET `student_name`='" + name + "' WHERE `student_id`='" + selectedStudentId + "'");
                    }

                } else {
                    //if there is no matches found insert a the address to the student then get the addressid
                    //update the student address id with the new one
                    ArrayList<String> info = new ArrayList<String>();
                    info.add(add1);
                    info.add(add2);
                    info.add(cityId);
                    InsertTable it = new InsertTable("address", info);

                    rs = MySql.sq("SELECT `address_id` FROM `address` WHERE "
                            + "`address_line_1`='" + add1 + "' AND  `address_line_2`='" + add2 + "' AND `city_id`='" + cityId + "'");
                    rs.next();
                    String addressId = rs.getString("address_id");
                    MySql.iud("UPDATE `student` SET `student_name`='" + name + "',`address_id`='" + addressId + "' WHERE `student_id`='" + selectedStudentId + "'");
                }
                //empty input fields
                JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jDateChooser1};
                SetEmptyItems.emptyItems(jp);
                //laod the tables
                loadTable();

                jButton1.setEnabled(true);
                jButton2.setEnabled(false);
                jTextField3.setEditable(true);
                jDateChooser1.setEnabled(true);
                jTable1.getSelectionModel().removeSelectionInterval(0, jTable1.getRowCount());

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StudentReg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(StudentReg.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jDateChooser1};
        int clickcount = evt.getClickCount();
        if (clickcount == 2) {
            jButton1.setEnabled(true);
            jButton2.setEnabled(false);
            jTextField3.setEditable(true);
            jDateChooser1.setEnabled(true);
            jTable1.getSelectionModel().removeSelectionInterval(0, jTable1.getRowCount());
            //set to defalut values 
            SetEmptyItems.emptyItems(jp);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        basicSearch();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:
        String nameText = jTextField5.getText() + evt.getKeyChar();

        basicSearch(nameText, jTextField6.getText());


    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        // TODO add your handling code here:
        String nameText = jTextField6.getText() + evt.getKeyChar();
        if (nameText.equals("") && jTextField5.getText().equals("")) {

            loadTable();
        } else {
            basicSearch(jTextField5.getText(), nameText);
        }

    }//GEN-LAST:event_jTextField6KeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jTextField5.setText("");
        jTextField6.setText("");
        jComboBox2.setSelectedIndex(0);
        loadTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField6KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(new Home());
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StudentReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentReg().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
