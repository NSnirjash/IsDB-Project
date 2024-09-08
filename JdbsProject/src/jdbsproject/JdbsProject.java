package jdbsproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DbUtil;

public class JdbsProject {

    static PreparedStatement ps;
    static ResultSet rs;
    static DbUtil db = new DbUtil();

    public static void main(String[] args) {
        saveData();
        showData();    
    }

    public static void saveData() {
        String insertSql = "insert into student(name,email,address,cell,position) values(?,?,?,?,?)";
        try {
            ps = db.getCon().prepareStatement(insertSql);

            ps.setString(1, "Nirob");
            ps.setString(2, "Nirob@gmail.com");
            ps.setString(3, "Mohammadpur,Dhaka");
            ps.setString(4, "01784991229");
            ps.setString(5, "First");

            ps.executeUpdate();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(JdbsProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showData() {
        String selectSql = "select * from student";
        try {
            ps = db.getCon().prepareStatement(selectSql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String cell = rs.getString("cell");
                String position = rs.getString("position");
                
                System.out.println("Id is " + id + " name: " + name + " Email: " + email
                        + " Address: " + address + " Cell No :" + cell+" position: "+position);
            }
            ps.close();
            rs.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(JdbsProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
