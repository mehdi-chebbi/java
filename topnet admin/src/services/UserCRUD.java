/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import  entities.user;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextField;
import tools.MyConnection;
/**
 *
 * @author DELL
 */
public class UserCRUD {
    public void addUser(user user) {
    try {
        String query = "INSERT INTO user (login, psw, nom, prenom, createdby, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, user.getLogin());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getFirstName());
        pst.setString(4, user.getLastName());
        pst.setString(5, user.getCreatedBy());
        pst.setString(6, user.getRole());
        pst.executeUpdate();
        System.out.println("User added successfully!");
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
}
public void deleteUserById(int userId) throws SQLException {
    try {
        String query = "DELETE FROM user WHERE Iduser = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setInt(1, userId);
        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
}
public void editUserRole(int userId, String newRole) {
    try {
        String query = "UPDATE user SET role = ? WHERE Iduser = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, newRole);
        pst.setInt(2, userId);
        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("User with ID " + userId + " role updated successfully!");
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
}


public boolean verifyCredentials(String login, String password) {
    try {
        String query = "SELECT * FROM user WHERE login = ? AND psw = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, login);
        pst.setString(2, password);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Credentials are correct
            return true;
        } else {
            // Credentials are incorrect
            return false;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        return false;
    }
}
public String getUserRole(String login) {
    try {
        String query = "SELECT role FROM user WHERE login = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, login);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Retrieve the role from the result set
            return rs.getString("role");
        } else {
            // User not found
            return null;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        return null;
    }
}

public String getCreatedBy(String login) {
    try {
        String query = "SELECT createdby FROM user WHERE login = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, login);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Retrieve the 'createdby' value from the result set
            return rs.getString("createdby");
        } else {
            // User not found
            return null;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        return null;
    }
}
 public List<user> recuperer() {
    List<user> userList = new ArrayList<>();
    String query = "SELECT * FROM user";

    try {
        PreparedStatement statement = MyConnection.getInstance().getCnx().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            user u = new user(); // Create a new user object for each iteration

            Timestamp createdOn = Timestamp.valueOf(resultSet.getString("createdOn"));
            Timestamp modifiedOn = resultSet.getString("modifiedOn") != null ? Timestamp.valueOf(resultSet.getString("modifiedOn")) : null;

            u.setIdUser(resultSet.getInt("idUser"));
            u.setLogin(resultSet.getString("login"));
            u.setPassword(resultSet.getString("psw"));
            u.setFirstName(resultSet.getString("nom"));
            u.setLastName(resultSet.getString("prenom"));
            u.setRole(resultSet.getString("role"));
            u.setCreatedOn(createdOn);
            u.setModifiedOn(modifiedOn);

            String createdBy = resultSet.getString("createdBy");
            if (createdBy != null) {
                u.setCreatedBy(createdBy);
            }

            String modifiedBy = resultSet.getString("modifiedBy");
            if (modifiedBy != null) {
                u.setModifiedBy(modifiedBy);
            }

            userList.add(u);
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage() + " Error!");
    }

    return userList;
}
 public void editUser(user userObj) throws SQLException {
    try {
        String query = "UPDATE user SET login = ?, psw = ?, nom = ?, prenom = ?, role = ?, modifiedOn = ?, modifiedBy = ? WHERE Iduser = ?";
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
        pst.setString(1, userObj.getLogin());
        pst.setString(2, userObj.getPassword());
        pst.setString(3, userObj.getFirstName());
        pst.setString(4, userObj.getLastName());
        pst.setString(5, userObj.getRole());
        pst.setTimestamp(6, userObj.getModifiedOn());
        pst.setString(7, userObj.getModifiedBy());
        pst.setInt(8, userObj.getIdUser());
        
        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("User with ID " + userObj.getIdUser() + " updated successfully!");
        } else {
            System.out.println("User not found with ID: " + userObj.getIdUser());
        }
    } catch (SQLException ex) {
        System.err.println("Error updating user: " + ex.getMessage());
        ex.printStackTrace(); // Print the stack trace for debugging purposes
    }
}





}
