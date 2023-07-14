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
public void deleteUserById(int userId) {
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


}
