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
    }
    
    
    
    
    

  @FXML
private void sign_in(ActionEvent event) {
    String username = username_field.getText();
    String password = pass_field.getText();

    UserCRUD userCRUD = new UserCRUD();
    user loggedUser = userCRUD.verifyCredentials(username, password);
    
    if (loggedUser != null) {
        if (loggedUser.getRole().equals("ADMIN")) {
            try { 
                UserSession session = UserSession.getInstance();
                session.setLoggedInUser(loggedUser); 
                System.out.println(loggedUser);
                
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
            a1.setContentText("Insufficient privileges. Only users with the ADMIN role can access the interface.!");
            a1.show();
            System.out.println("non");
        }
    } else {
            Alert a2 = new Alert(Alert.AlertType.ERROR);
            a2.setTitle("Error");
            a2.setContentText("Invalid login credentials. Please check your username and password.!");
            a2.show();
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
    
    
    
        public static void sendConfirmationEmail(String username, String email) {
        // Gmail account details
        final String usernamee = "mehdi.chebbi@gmail.com";
        final String password = "azeqsdwxc123";

        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a Session object
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usernamee, password);
            }
        });

        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(usernamee));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Set the subject and content
            message.setSubject("Confirmation Email");
            message.setText(String.format("Dear %s,\n\nThank you for signing up! Your account has been successfully created.", usernamee));

            // Send the email
            Transport.send(message);
            System.out.println("Confirmation email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send confirmation email. Error: " + e.getMessage());
        }
    }
}
