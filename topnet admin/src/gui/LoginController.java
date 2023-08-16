/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.UserSession;
import entities.user;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import services.UserCRUD;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javax.mail.*;
import javax.mail.internet.*;
/**
 * FXML Controller class
 *
 * @author DELL
 */
public class LoginController implements Initializable {

        @FXML
        private TextField pass_field;
        @FXML
        private TextField username_field;
        @FXML
        private Hyperlink recover_link;
        @FXML
        private Button sign_in_button;
    @FXML
    private Hyperlink register_link;
    @FXML
    private Label error_label;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       UserCRUD uc = new UserCRUD();
       System.out.print(uc.getCreatedBy("mehdi"));
        
    }    

    @FXML
    private void recover_password(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("recover.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new FXML file as the content of the stage
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    
    
    
    

 @FXML
private void sign_in(ActionEvent event) {
    String username = username_field.getText();
    String password = pass_field.getText();

    UserCRUD userCRUD = new UserCRUD();
    user loggedUser = userCRUD.verifyCredentials(username, password);
    
    if (loggedUser != null) {
        if (loggedUser.getRole().equals("ADMIN")) {
            // Check if the 'createdby' attribute is null
            if (loggedUser.getCreatedBy() != null) {
                try { 
               
                    UserSession session = UserSession.getInstance();
                    session.setLoggedInUser(loggedUser); 
                    System.out.println(loggedUser);
                    session.setIsLoggedIn(true);
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("home_admin.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    ((Stage) sign_in_button.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert a1 = new Alert(Alert.AlertType.ERROR);
                a1.setTitle("Error");
                a1.setContentText("Account is not activated by admin yet!");
                a1.show();
                System.out.println("non");
            }
        } else {
            Alert a2 = new Alert(Alert.AlertType.ERROR);
            a2.setTitle("Error");
            a2.setContentText("Insufficient privileges. Only users with the ADMIN role can access the interface!");
            a2.show();
            System.out.println("non");
        }
    } else {
        Alert a3 = new Alert(Alert.AlertType.ERROR);
        a3.setTitle("Error");
        a3.setContentText("Invalid login credentials. Please check your username and password!");
        a3.show();
        System.out.println("non");
    } 
}

    @FXML
    private void register(ActionEvent event) {
         try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new FXML file as the content of the stage
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    
    
      
}
