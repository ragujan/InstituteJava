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
public class TeacherReg extends javax.swing.JFrame {

    /**
     * Creates new form TeacherReg
     */
    public TeacherReg() {
        initComponents();
        setValidation();
        LoadCatsNBrands lcnb = new LoadCatsNBrands();
        lcnb.loadCities(jComboBox1);
        lcnb.loadSubject(jComboBox3);
        loadTable();
        tableListernRag();
        jButton2.setEnabled(false);

    }

    public TeacherReg(ClassReg cr) {
        this();
        this.cr = cr;
        isClassRegInvolved = true;
        this.thistr = this;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public TeacherReg(AdvancedSearch as) {
        this();
        this.as = as;
        isAdvancedSearchInvolved = true;
        this.thistr = this;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    String selectedTeacherId;
    String whereQuery = "";

    TeacherReg thistr;
    boolean isClassRegInvolved;
    ClassReg cr;
    boolean isAdvancedSearchInvolved;
    AdvancedSearch as;
    String[] colnames = {"teacher_id", "teacher_name", "teacher_contact", "subject_name", "address", "city_name"};
    String loadTableQuery = "SELECT \n"
            + "teacher.teacher_id,\n"
            + "teacher.teacher_name,\n"
            + "teacher.teacher_contact,\n"
            + "CONCAT(address_teacher.address_line_1,' ',address_teacher.address_line_2)  AS `address`,\n"
            + "city.city_name,\n"
            + "subject.subject_name\n"
            + "FROM teacher \n"
            + "INNER JOIN address_teacher\n"
            + "ON address_teacher.address_id = teacher.address_id\n"
            + "INNER JOIN subject\n"
            + "ON subject.subject_id = teacher.subject_id\n"
            + "INNER JOIN city \n"
            + "ON city.city_id = address_teacher.city_id ";

    public void basicSearch(String st, String pn) {
        String sort = jComboBox5.getSelectedItem().toString();
        String searchText = st;
        String pnumber = pn;
        String sortQuery = null;
        if (searchText.isEmpty() && pnumber.isEmpty()) {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` ASC";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` DESC";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` DESC";
            }
            StringBuilder stringquerybuild = new StringBuilder();
            stringquerybuild.append(this.loadTableQuery).toString();
            stringquerybuild.append(sortQuery).toString();
            String query = stringquerybuild.toString();

            LoadTables lt = new LoadTables(jTable1, query, colnames);
        } else {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` ASC ";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` DESC ";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC ";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC ";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` ASC ";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` DESC ";
            }
            StringBuilder stringquerybuild = new StringBuilder();

            StringBuilder whereQueryBuilder = new StringBuilder();
            Vector<String> v = new Vector<String>();

            if (!searchText.isEmpty()) {
                String descWhere = "`teacher_name` LIKE '%" + searchText + "%' ";
                v.add(descWhere);
            }
            if (!pnumber.isEmpty()) {
                String teacherWhere = "`teacher_contact` LIKE '%" + pnumber + "%' ";
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
        String sort = jComboBox5.getSelectedItem().toString();
        String searchText = jTextField5.getText();
        String pnumber = jTextField6.getText();
        String sortQuery = null;
        if (searchText.isEmpty() && pnumber.isEmpty()) {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` ASC";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` DESC";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` ASC";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` DESC";
            }
            StringBuilder stringquerybuild = new StringBuilder();
            stringquerybuild.append(this.loadTableQuery).toString();
            stringquerybuild.append(sortQuery).toString();
            String query = stringquerybuild.toString();

            LoadTables lt = new LoadTables(jTable1, query, colnames);
        } else {
            if (sort.equals("NAME AZ")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` ASC ";

            } else if (sort.equals("NAME ZA")) {
                sortQuery = "ORDER BY `teacher`.`teacher_name` DESC ";
            } else if (sort.equals("CITY AZ")) {
                sortQuery = "ORDER BY `city`.`city_name` ASC ";
            } else if (sort.equals("CITY ZA")) {
                sortQuery = "ORDER BY `city`.`city_name` DESC ";
            } else if (sort.equals("ADDRESS AZ")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` ASC ";
            } else if (sort.equals("ADDRESS ZA")) {
                sortQuery = "ORDER BY `address_teacher`.`address_line_1` DESC ";
            }
            StringBuilder stringquerybuild = new StringBuilder();

            StringBuilder whereQueryBuilder = new StringBuilder();
            Vector<String> v = new Vector<String>();

            if (!searchText.isEmpty()) {
                String descWhere = "`teacher_name` LIKE '%" + searchText + "%' ";
                v.add(descWhere);
            }
            if (!pnumber.isEmpty()) {
                String teacherWhere = "`teacher_contact` LIKE '%" + pnumber + "%' ";
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
        String sort = "ORDER BY `teacher`.`teacher_name` ASC";

        StringBuilder stringquerybuild = new StringBuilder();
        stringquerybuild.append(this.loadTableQuery).toString();
        stringquerybuild.append(sort).toString();
        String query = stringquerybuild.toString();
        String[] colnames = {"teacher_id", "teacher_name", "teacher_contact", "subject_name", "address", "city_name"};

        LoadTables lt = new LoadTables(jTable1, query, colnames);

    }

    public void tableListernRag() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {

                    String tid = jTable1.getValueAt(row, 0).toString();
                    String tname = jTable1.getValueAt(row, 1).toString();
                    String tcontact = jTable1.getValueAt(row, 2).toString();
                    String tsub = jTable1.getValueAt(row, 3).toString();
                    String address = jTable1.getValueAt(row, 4).toString();
                    String city = jTable1.getValueAt(row, 5).toString();
                    String[] addressSplit = address.split(" ");
                    String add1 = addressSplit[0];
                    String add2 = addressSplit[1];
                    jTextField1.setText(tname);
                    jTextField3.setText(tcontact);
                    jComboBox1.setSelectedItem(city);
                    jComboBox3.setSelectedItem(tsub);
                    jTextField4.setText(add1);
                    jTextField2.setText(add2);
                    selectedTeacherId = tid;
                    if (isClassRegInvolved) {
                        cr.tid.setText(tid);
                        cr.tname.setText(tname);
                        cr.selectedTeacherSubjectId = tsub;
                        thistr.dispose();

                    }
                    if (isAdvancedSearchInvolved) {
                        as.searchtname.setText(tname);
                        thistr.dispose();
                    }
                    //  jDateChooser1.setDate(sdf.parse(sdob));
                    //  Date d = new Date
                    //jDateChooser1.setDate()
                    jButton2.setEnabled(true);
                    jButton1.setEnabled(false);
                    jTextField3.setEditable(false);

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
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

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

        jLabel8.setText("Subject");
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

        jComboBox3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox3MouseClicked(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel14.add(jComboBox3);

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

        jButton6.setText("Select Subject");
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
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                "T_ID", "T_Name", "T_Contact", "T_Subject", "Address", "City"
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

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NAME AZ", "NAME ZA", "CITY AZ", "CITY ZA", "ADDRESS AZ", "ADDRESS ZA" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        jComboBox5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox5PropertyChange(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
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
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField6KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel1.setText("Sort");

        jLabel2.setText("Name");

        jLabel3.setText("Phone");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    ///enter teacher
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String name = jTextField1.getText();
        String contact = jTextField3.getText();
        String contactregex = "([0][7][24-8][0-9]{7})";

        String add1 = jTextField4.getText();
        String add2 = jTextField2.getText();
        String subject = jComboBox3.getSelectedItem().toString();
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
        } else {
            try {
                ResultSet rs = MySql.sq("SELECT * FROM `teacher` WHERE `teacher_contact`='" + contact + "'");

                if (rs.next()) {
                    JOP.setJOPMessage(this, "This contact number is already taken", "Warning", 1);
                } else {

                    rs = MySql.sq("SELECT `city_id` FROM `city` WHERE `city_name`='" + city + "'");
                    rs.next();
                    String cityId = rs.getString("city_id");
                    rs = MySql.sq("SELECT `subject_id` FROM `subject` WHERE `subject_name`='" + subject + "'");
                    rs.next();
                    String subjectId = rs.getString("subject_id");

                    ArrayList<String> info = new ArrayList<String>();
                    info.add(add1);
                    info.add(add2);
                    info.add(cityId);
                    InsertTable it = new InsertTable("address_teacher", info);
                    rs = MySql.sq("SELECT `address_id` FROM `address_teacher` WHERE "
                            + "`address_line_1`='" + add1 + "' AND  `address_line_2`='" + add2 + "' AND `city_id`='" + cityId + "'");
                    rs.next();
                    String addressId = rs.getString("address_id");
                    info = new ArrayList<String>();
                    info.add(addressId);
                    info.add(subjectId);
                    info.add(contact);
                    info.add(name);

                    new InsertTable("teacher", info);
                    JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jComboBox3};
                    SetEmptyItems.emptyItems(jp);
                    loadTable();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TeacherReg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TeacherReg.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton1ActionPerformed
    ////update 
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String name = jTextField1.getText();
        String contact = jTextField3.getText();
        String contactregex = "([0][7][24-8][0-9]{7})";
        String subject = jComboBox3.getSelectedItem().toString();
        JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jComboBox3};
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
        } else {

            try {
                //search query for getting the city id
                ResultSet rs = MySql.sq("SELECT `city_id` FROM `city` WHERE `city_name`='" + city + "'");

                rs.next();
                //got the city id
                String cityId = rs.getString("city_id");

                rs = MySql.sq("SELECT `subject_id` FROM `subject` WHERE `subject_name`='" + subject + "'");
                rs.next();
                //got the subject id
                String subjectId = rs.getString("subject_id");
                //"`address_teacher`";

                //check for address if its already exists
                ResultSet addressCheck = MySql.sq("SELECT * FROM `address_teacher` INNER JOIN `city` ON `city`.`city_id`=`address_teacher`.`city_id` "
                        + "WHERE `address_line_1`='" + add1 + "' AND `address_line_2`='" + add2 + "' AND `city_name`='" + city + "'  ");
                //if it does exits just give them a warning just update the name
                if (addressCheck.next()) {
                    String alreadyExistingAddressId = addressCheck.getString("address_id");
                    int op = JOptionPane.showConfirmDialog(this, "This address already exists do you want to set it as a your address ", "Duplicate GRN entry", JOptionPane.YES_NO_OPTION);
                    if (op == 0) {
                        MySql.iud("UPDATE `teacher` SET `teacher_name`='" + name + "',`address_id`='" + alreadyExistingAddressId + "' WHERE `teacher_id`='" + selectedTeacherId + "'");
                    } else {
                        MySql.iud("UPDATE `teacher` SET `teacher_name`='" + name + "' WHERE `teacher_id`='" + selectedTeacherId + "'");
                    }

                } else {
                    //if there is no matches found insert a the address to the teacher then get the addressid
                    //update the teacher address id with the new one
                    ArrayList<String> info = new ArrayList<String>();
                    info.add(add1);
                    info.add(add2);
                    info.add(cityId);
                    InsertTable it = new InsertTable("address_teacher", info);

                    rs = MySql.sq("SELECT `address_id` FROM `address_teacher` WHERE "
                            + "`address_line_1`='" + add1 + "' AND  `address_line_2`='" + add2 + "' AND `city_id`='" + cityId + "'");
                    rs.next();
                    String addressId = rs.getString("address_id");
                    MySql.iud("UPDATE `teacher` SET `teacher_name`='" + name + "',`address_id`='" + addressId + "' WHERE `teacher_id`='" + selectedTeacherId + "'");
                }
                //empty input fields

                SetEmptyItems.emptyItems(jp);
                //laod the tables
                loadTable();

                jButton1.setEnabled(true);
                jButton2.setEnabled(false);
                jTextField3.setEditable(true);

                jTable1.getSelectionModel().removeSelectionInterval(0, jTable1.getRowCount());

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TeacherReg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TeacherReg.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        JComponent[] jp = {jTextField1, jTextField2, jTextField3, jTextField4, jComboBox1, jComboBox3};
        int clickcount = evt.getClickCount();
        if (clickcount == 2) {
            jButton1.setEnabled(true);
            jButton2.setEnabled(false);
            jTextField3.setEditable(true);
            jTable1.getSelectionModel().removeSelectionInterval(0, jTable1.getRowCount());
            //set to defalut values 
            SetEmptyItems.emptyItems(jp);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox5PropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBox5PropertyChange

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
        this.basicSearch();
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:
        basicSearch(jTextField5.getText() + evt.getKeyChar(), jTextField6.getText());
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        // TODO add your handling code here:
        basicSearch(jTextField5.getText(), jTextField6.getText() + evt.getKeyChar());
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField6KeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        jTextField6.setText("");
        jTextField5.setText("");
        jComboBox5.setSelectedIndex(0);
        loadTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(this,new SubjectReg(this),false);
      
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CreateObject.make(new Home());
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox3MouseClicked
        // TODO add your handling code here:
           
          
      
    }//GEN-LAST:event_jComboBox3MouseClicked

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
            java.util.logging.Logger.getLogger(TeacherReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TeacherReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TeacherReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeacherReg.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TeacherReg().setVisible(true);
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
    public javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
