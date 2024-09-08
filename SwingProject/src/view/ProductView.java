/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DbUtil;

/**
 *
 * @author User
 */
public class ProductView extends javax.swing.JFrame {

    DbUtil db = new DbUtil();
    PreparedStatement ps;
    ResultSet rs;
    
    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);

    /**
     * Creates new form ProductView
     */
    public ProductView() {
        initComponents();
        showProductOnTable();
        showProductToCombo();
        showStockOnTable();

        comProductName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getProductSalesPrice(e);
            }
        });
    }
    
    public void getGrossProfit() {
        
        java.util.Date toDate = dateToReport.getDate();
        java.util.Date fromDate = dateFromReport.getDate();

        String sql1 = "select sum(salesTotalPrice) from sales where salesDate between ? and ?";

        String sql = "select sum(totalPrice) from product where purcahseDate between ? and ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            float totalPurchasePrice = 0;

            while (rs.next()) {

                //int id = rs.getInt("id");
                totalPurchasePrice = rs.getFloat("sum(totalPrice)");

            }

            ps.close();
            rs.close();
            db.getCon().close();

            
            // For Sales 
            
            ps = db.getCon().prepareStatement(sql1);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();
            float totalSalesPrice=0;
            
            while (rs.next()) {
                
                 totalSalesPrice = rs.getFloat("sum(salesTotalPrice)");
               
            }

            ps.close();
            rs.close();
            db.getCon().close();
            
            float grossProfit= totalSalesPrice-totalPurchasePrice;
            
            lblProfit.setText("Profit: "+grossProfit);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    public void getSalesReport() {

        String[] salesViewTableColumn = {"SL", "Name", "Unit Price", "Qunatity", "Total Price", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(salesViewTableColumn);
        tblReports.setModel(model);

        java.util.Date toDate = dateToReport.getDate();
        java.util.Date fromDate = dateFromReport.getDate();

        String sql = "select * from sales where salesDate between ? and ?";

        String sql1 = "select sum(salesTotalPrice) from sales where salesDate between ? and ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            int sl = 1;

            while (rs.next()) {

                //int id = rs.getInt("id");
                String name = rs.getString("name");
                float unitPrice = rs.getFloat("salesUnitPrice");
                float quantity = rs.getFloat("salesQuantity");
                float totalPrice = rs.getFloat("salesTotalPrice");

                Date salesDate = rs.getDate("salesDate");

                model.addRow(new Object[]{sl, name, unitPrice, quantity, totalPrice, salesDate});

                sl += 1;
            }

            ps.close();
            rs.close();
            db.getCon().close();

            ps = db.getCon().prepareStatement(sql1);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {
                float totalPrice = rs.getFloat("sum(salesTotalPrice)");
                System.out.println(totalPrice);
                model.addRow(new Object[]{"", "", "", "Total Amount", totalPrice});
            }

            ps.close();
            rs.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getPurchaseReport() {

        String[] productViewTableColumn = {"SL", "Name", "Unit Price", "Qunatity", "Total Price", "Sales Price", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productViewTableColumn);
        tblReports.setModel(model);

        java.util.Date fromDate = dateFromReport.getDate();
        java.util.Date toDate = dateToReport.getDate();

//        Date toDate = dateToReport.getDate();
//        Date fromDate = dateFromReport.getDate();
        String sql = "select * from product where purcahseDate between ? and ?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            int sl = 1;

            while (rs.next()) {

                //int id = rs.getInt("id");
                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                float salesPrice = rs.getFloat("salesPrice");
                Date purcahseDate = rs.getDate("purcahseDate");

                model.addRow(new Object[]{sl, name, unitPrice, quantity, totalPrice, salesPrice, purcahseDate});

                sl += 1;
            }

            ps.close();
            rs.close();
            db.getCon().close();

            String sql1 = "select sum(totalPrice) from product where purcahseDate between ? and ?";

            ps = db.getCon().prepareStatement(sql1);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {

                //int id = rs.getInt("id");
                float totalPrice = rs.getFloat("sum(totalPrice)");

                model.addRow(new Object[]{"", "", "", "Total Amount", totalPrice});

            }

            ps.close();
            rs.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean getStocProductList() {
        String sql = "select name from stock";
        boolean status = false;
        String purchaseProductName = txtName.getText().trim();

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                if (productName.equalsIgnoreCase(purchaseProductName)) {
                    status = true;
                    break;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public void addProductToStock() {
        boolean status = getStocProductList();
        if (status) {
            String sql = "update stock set quantity=quantity+? where name=?";

            try {
                ps = db.getCon().prepareStatement(sql);

                ps.setFloat(1, Float.parseFloat(txtQuantity.getText().trim()));
                ps.setString(2, txtName.getText().trim());

                ps.executeUpdate();
                ps.close();
                db.getCon().close();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String sql = "insert into stock(name,purcahsePrice,quantity) values (?,?,?)";
            PreparedStatement ps;

            try {
                ps = db.getCon().prepareStatement(sql);
                ps.setString(1, txtName.getText().trim());
                ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
                ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));

                ps.executeUpdate();
                ps.close();
                db.getCon().close();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addProduct() {
        String sql = "insert into product(name,unitPrice,quantity,totalPrice,salesPrice,purcahseDate) values (?,?,?,?,?,?)";
        PreparedStatement ps;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));
            ps.setDate(6, sqlCurrentDate);

            ps.executeUpdate();
            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Add Product Successfully");
            addProductToStock();
            clear();
            showProductOnTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Add Product unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTotalPrice() {
        float unitPrice = Float.parseFloat(txtUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtQuantity.getText().trim());
        float totalPrice = unitPrice * quantity;
        txtTotalPrice.setText(totalPrice + "");
    }

    public void clear() {
        txtId.setText("");
        txtName.setText("");
        txtUnitPrice.setText("");
        txtQuantity.setText("");
        txtTotalPrice.setText("");
        txtSalesPrice.setText("");
    }

    String[] productViewTableColumn = {"id", "Name", "Unit Price", "Quantity", "Total Price", "Sales Price"};
    String[] stockViewTableColumn = {"id", "Name", "Unit Price", "Quantity"};

    public void showProductOnTable() {
        String sql = "select * from product";
        PreparedStatement ps;
        ResultSet rs;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productViewTableColumn);

        tblProductView.setModel(model);
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                float salesPrice = rs.getFloat("salesPrice");

                model.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, salesPrice});
            }
            rs.close();
            ps.close();
            db.getCon();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showStockOnTable() {
        String sql = "select * from stock";
        PreparedStatement ps;
        ResultSet rs;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(stockViewTableColumn);

        tblStock.setModel(model);
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float quantity = rs.getFloat("quantity");
                float unitPrice = rs.getFloat("purcahsePrice");

                model.addRow(new Object[]{id, name, unitPrice, quantity});
            }
            rs.close();
            ps.close();
            db.getCon();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteProduct() {
        String sql = "delete from product where id = ?";
        PreparedStatement ps;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            ps.close();
            db.getCon();
            JOptionPane.showMessageDialog(this, "Delete Product Successfully");
            clear();
            showProductOnTable();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editProduct() {
        String sql = "update product set name = ?, unitPrice = ?, quantity = ?,totalPrice = ?, salesPrice = ? where id = ?";
        PreparedStatement ps;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();
            ps.close();
            db.getCon();
            JOptionPane.showMessageDialog(this, "Update Product Successfully");
            clear();
            showProductOnTable();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Update Product unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update Product unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showProductToCombo() {
        String sql = "select name from product";
        PreparedStatement ps;
        ResultSet rs;
        comProductName.removeAllItems();

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                comProductName.addItem(productName);
            }

            ps.close();
            db.getCon().close();
            rs.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getProductSalesPrice(ItemEvent e) {
        String selectedProductName = "";
        if (e.getStateChange() == ItemEvent.SELECTED) {
            selectedProductName = (String) e.getItem();
            extractSalesPrice(selectedProductName);
        }

        String sql = "Select quantity from stock where name=?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, selectedProductName);

            rs = ps.executeQuery();

            while (rs.next()) {
                float quantity = rs.getFloat("quantity");
                lblStock.setText(quantity + "");

            }

            ps.close();
            db.getCon().close();
            rs.close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void extractSalesPrice(String productName) {
        String sql = "select salesPrice from product where name=?";
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, productName);
            rs = ps.executeQuery();
            while (rs.next()) {
                String salesPrice = rs.getString("salesPrice");
                txtSalesUnitPrice.setText(salesPrice);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTOtalSalesPrice() {
        float quantity = Float.parseFloat(txtSalesQuantity.getText().toString().trim());
        float unitPrice = Float.parseFloat(txtSalesUnitPrice.getText().toString().trim());
        float salesTotalPrice = quantity * unitPrice;
        txtSalesTotalPrice.setText(salesTotalPrice + "");
    }

    public String formatDateToDDMMYYYY(JDateChooser date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }

    public static java.sql.Date convertStringToSqlDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            java.util.Date utilDate = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = outputFormat.format(utilDate);
            return java.sql.Date.valueOf(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stockUpdateOnSales() {
        String sql = "update stock set quantity=quantity-? where name=?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtSalesQuantity.getText().trim()));
            ps.setString(2, comProductName.getSelectedItem().toString());

            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addSales() {
        Date date = convertUtilDateToSqlDate(salesDate.getDate());
        PreparedStatement ps;
        String sql = "insert into sales(name,salesUnitPrice,salesQuantity,salesTotalPrice,SalesDate) values(?,?,?,?,?)";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, comProductName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtSalesUnitPrice.getText()));
            ps.setFloat(3, Float.parseFloat(txtSalesQuantity.getText()));
            ps.setFloat(4, Float.parseFloat(txtSalesTotalPrice.getText()));
            ps.setDate(5, convertUtilDateToSqlDate(date));

            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(this, "ADD sales Successfully");
            stockUpdateOnSales();
            showStockOnTable();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "ADD sales unsuccessfully");
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnAddProduct = new javax.swing.JButton();
        btnSalesProduct = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        mainView = new javax.swing.JTabbedPane();
        add = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSalesPrice = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductView = new javax.swing.JTable();
        sales = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        comProductName = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtSalesQuantity = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        salesDate = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        txtSalesUnitPrice = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtSalesTotalPrice = new javax.swing.JTextField();
        btnSalesSave = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        stock = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        report = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dateFromReport = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        dateToReport = new com.toedter.calendar.JDateChooser();
        btnReportPurchase = new javax.swing.JButton();
        btnReportSale = new javax.swing.JButton();
        btnReportGrossProfit = new javax.swing.JButton();
        btnReportReset = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReports = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblProfit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Juice ITC", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("JEE 59 Shop");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 100));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 100));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAddProduct.setBackground(new java.awt.Color(213, 231, 234));
        btnAddProduct.setFont(new java.awt.Font("NSimSun", 3, 18)); // NOI18N
        btnAddProduct.setText("Add Product");
        btnAddProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddProductMouseClicked(evt);
            }
        });
        jPanel3.add(btnAddProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 40));

        btnSalesProduct.setBackground(new java.awt.Color(213, 231, 234));
        btnSalesProduct.setFont(new java.awt.Font("NSimSun", 3, 18)); // NOI18N
        btnSalesProduct.setText("Sales Product");
        btnSalesProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesProductMouseClicked(evt);
            }
        });
        btnSalesProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesProductActionPerformed(evt);
            }
        });
        jPanel3.add(btnSalesProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 190, 40));

        btnStock.setBackground(new java.awt.Color(213, 231, 234));
        btnStock.setFont(new java.awt.Font("NSimSun", 3, 18)); // NOI18N
        btnStock.setText("Stock");
        btnStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockMouseClicked(evt);
            }
        });
        jPanel3.add(btnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 190, 40));

        btnReport.setBackground(new java.awt.Color(213, 231, 234));
        btnReport.setFont(new java.awt.Font("NSimSun", 3, 18)); // NOI18N
        btnReport.setText("Report");
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        jPanel3.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 190, 40));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, 600));

        add.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Add Product");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 70));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 70));

        jLabel6.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel6.setText("ID");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, 30));

        txtId.setEditable(false);
        jPanel4.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 240, 40));

        jLabel7.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel7.setText("Name");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 80, 30));

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        jPanel4.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 240, 40));

        jLabel8.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel8.setText("Unit Price");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 40));
        jPanel4.add(txtUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 240, 40));

        jLabel9.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel9.setText("Quantity");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 100, 40));

        txtQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantityFocusLost(evt);
            }
        });
        jPanel4.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 240, 40));

        jLabel10.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel10.setText("Total Price");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 110, 40));

        txtTotalPrice.setEditable(false);
        jPanel4.add(txtTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 240, 40));

        jLabel11.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel11.setText("Sales Price");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 110, 40));
        jPanel4.add(txtSalesPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 240, 40));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton1.setText("Add");
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
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 90, 30));

        jButton2.setBackground(new java.awt.Color(255, 204, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton2.setText("Delete");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, 90, 30));

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton3.setText("Edit");
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 90, 30));

        jButton4.setBackground(new java.awt.Color(255, 204, 204));
        jButton4.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton4.setText("Reset");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 170, 90, 30));

        tblProductView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProductView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductView);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 830, 240));

        add.add(jPanel4, java.awt.BorderLayout.CENTER);

        mainView.addTab("Add", add);

        sales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("NSimSun", 3, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Sales Product");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 100));

        sales.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 100));

        jLabel12.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel12.setText("Name");
        sales.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 60, 40));

        comProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sales.add(comProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 260, 40));

        jLabel13.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel13.setText("Quantity");
        sales.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 80, 40));

        txtSalesQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesQuantityFocusLost(evt);
            }
        });
        sales.add(txtSalesQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 260, 40));

        jLabel14.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel14.setText("Date");
        sales.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, 50, 30));
        sales.add(salesDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 132, 200, 40));

        jLabel15.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel15.setText("Unit Price");
        sales.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 40));

        txtSalesUnitPrice.setEditable(false);
        sales.add(txtSalesUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 260, 40));

        jLabel16.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel16.setText("Total Price");
        sales.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 100, 40));

        txtSalesTotalPrice.setEditable(false);
        sales.add(txtSalesTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, 260, 40));

        btnSalesSave.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnSalesSave.setText("Save");
        btnSalesSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSaveMouseClicked(evt);
            }
        });
        sales.add(btnSalesSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 90, 30));

        jButton6.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton6.setText("Edit");
        sales.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 90, 30));

        jButton7.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton7.setText("Reset");
        sales.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 380, 90, 30));

        jButton8.setBackground(new java.awt.Color(255, 204, 204));
        jButton8.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton8.setText("Delete");
        sales.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 380, 90, 30));

        jLabel17.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setText("Stock");
        sales.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 196, 60, 40));

        lblStock.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        lblStock.setForeground(new java.awt.Color(255, 0, 51));
        sales.add(lblStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 196, 180, 40));

        mainView.addTab("sales", sales);

        stock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        stock.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setFont(new java.awt.Font("Nirmala UI", 3, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Stock");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        stock.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 90));

        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblStock);

        stock.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 810, 460));

        mainView.addTab("stock", stock);

        report.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(153, 153, 153));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Report");
        jPanel9.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 80));

        report.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 80));

        jLabel18.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel18.setText("From");
        report.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 50, 40));
        report.add(dateFromReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 112, 240, 40));

        jLabel19.setFont(new java.awt.Font("NSimSun", 0, 18)); // NOI18N
        jLabel19.setText("To");
        report.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 30, 40));
        report.add(dateToReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 240, 40));

        btnReportPurchase.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnReportPurchase.setText("Purchase");
        btnReportPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportPurchaseMouseClicked(evt);
            }
        });
        report.add(btnReportPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 90, 30));

        btnReportSale.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnReportSale.setText("Sale");
        btnReportSale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportSaleMouseClicked(evt);
            }
        });
        report.add(btnReportSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 80, 30));

        btnReportGrossProfit.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnReportGrossProfit.setText("Gross Profit");
        btnReportGrossProfit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportGrossProfitMouseClicked(evt);
            }
        });
        report.add(btnReportGrossProfit, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 110, 30));

        btnReportReset.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnReportReset.setText("Reset");
        report.add(btnReportReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 90, 30));

        tblReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblReports);

        report.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 810, 340));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        report.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, 240, -1));

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        report.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, 240, -1));

        jPanel12.setBackground(new java.awt.Color(204, 255, 204));

        lblProfit.setFont(new java.awt.Font("NSimSun", 3, 26)); // NOI18N
        lblProfit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProfit.setText("Profit:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblProfit, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblProfit, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        report.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, 290, -1));

        mainView.addTab("report", report);

        getContentPane().add(mainView, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 810, 640));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalesProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalesProductActionPerformed

    private void btnAddProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddProductMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(0);
    }//GEN-LAST:event_btnAddProductMouseClicked

    private void btnSalesProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesProductMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(1);
        showProductToCombo();
    }//GEN-LAST:event_btnSalesProductMouseClicked

    private void btnStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(2);
    }//GEN-LAST:event_btnStockMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(3);
    }//GEN-LAST:event_btnReportMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        addProduct();
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantityFocusLost
        // TODO add your handling code here:
        getTotalPrice();
    }//GEN-LAST:event_txtQuantityFocusLost

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_jButton4MouseClicked

    private void tblProductViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductViewMouseClicked
        // TODO add your handling code here:
        int rowIndex = tblProductView.getSelectedRow();
        String id = tblProductView.getModel().getValueAt(rowIndex, 0).toString();
        String name = tblProductView.getModel().getValueAt(rowIndex, 1).toString();
        String unitPrice = tblProductView.getModel().getValueAt(rowIndex, 2).toString();
        String quantity = tblProductView.getModel().getValueAt(rowIndex, 3).toString();
        String totalPrice = tblProductView.getModel().getValueAt(rowIndex, 4).toString();
        String salesPrice = tblProductView.getModel().getValueAt(rowIndex, 5).toString();

        txtId.setText(id);
        txtName.setText(name);
        txtUnitPrice.setText(unitPrice);
        txtQuantity.setText(quantity);
        txtTotalPrice.setText(totalPrice);
        txtSalesPrice.setText(salesPrice);
    }//GEN-LAST:event_tblProductViewMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        deleteProduct();
    }//GEN-LAST:event_jButton2MouseClicked

    private void txtSalesQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesQuantityFocusLost
        // TODO add your handling code here:
        getTOtalSalesPrice();
    }//GEN-LAST:event_txtSalesQuantityFocusLost

    private void btnSalesSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSaveMouseClicked
        // TODO add your handling code here:
        addSales();
    }//GEN-LAST:event_btnSalesSaveMouseClicked

    private void btnReportPurchaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportPurchaseMouseClicked
        // TODO add your handling code here:
        getPurchaseReport();
    }//GEN-LAST:event_btnReportPurchaseMouseClicked

    private void btnReportSaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportSaleMouseClicked
        // TODO add your handling code here:
        getSalesReport();
    }//GEN-LAST:event_btnReportSaleMouseClicked

    private void btnReportGrossProfitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportGrossProfitMouseClicked
        // TODO add your handling code here:
        getGrossProfit();
    }//GEN-LAST:event_btnReportGrossProfitMouseClicked

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
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProductView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnReportGrossProfit;
    private javax.swing.JButton btnReportPurchase;
    private javax.swing.JButton btnReportReset;
    private javax.swing.JButton btnReportSale;
    private javax.swing.JButton btnSalesProduct;
    private javax.swing.JButton btnSalesSave;
    private javax.swing.JButton btnStock;
    private javax.swing.JComboBox<String> comProductName;
    private com.toedter.calendar.JDateChooser dateFromReport;
    private com.toedter.calendar.JDateChooser dateToReport;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblProfit;
    private javax.swing.JLabel lblStock;
    private javax.swing.JTabbedPane mainView;
    private javax.swing.JPanel report;
    private javax.swing.JPanel sales;
    private com.toedter.calendar.JDateChooser salesDate;
    private javax.swing.JPanel stock;
    private javax.swing.JTable tblProductView;
    private javax.swing.JTable tblReports;
    private javax.swing.JTable tblStock;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSalesPrice;
    private javax.swing.JTextField txtSalesQuantity;
    private javax.swing.JTextField txtSalesTotalPrice;
    private javax.swing.JTextField txtSalesUnitPrice;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
