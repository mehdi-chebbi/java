/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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

    // Perform authentication or other actions using the username and password

    // Example: Verify credentials using UserCRUD's verifyCredentials method
        UserCRUD userCRUD = new UserCRUD();
    boolean credentialsValid = userCRUD.verifyCredentials(username, password);
    if (credentialsValid) {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home_admin.fxml"));
            Parent root = loader.load();

            // Create a new stage and set the home.fxml scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current login stage (assuming the login stage is the owner of the event source)
            ((Stage) sign_in_button.getScene().getWindow()).close();
        } catch (IOException e) {
            // Handle any exception that occurs while loading the home.fxml file
            e.printStackTrace();
        }
    } else {
        // Credentials are incorrect, show an error message or handle accordingly
        System.out.println("Invalid login credentials. Please check your username and password.");
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
