/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UserCRUD;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * FXML Controller class
 *
 * @author mehdi
 */
public class RecoverController implements Initializable {

    @FXML
    private TextField recov_mail;
    @FXML
    private Button recover_button;
            UserCRUD uc = new UserCRUD();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
@FXML
private void recover_button_action(ActionEvent event) {
    boolean isCodeCorrect = false;

    String email = recov_mail.getText();

    // Email validation using regular expressions
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    boolean isValidEmail = email.matches(emailRegex);

    if (isValidEmail) {
        boolean userExists = uc.checkUserExists(email);
        if (userExists) {
            // Generate a verification code
            int verificationCode = send_mail(email, "User Name");

            boolean codeEnteredCorrectly = false;
            while (!codeEnteredCorrectly) {
                // Open a new window to enter the verification code
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Verification Code");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter the verification code:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    String enteredCode = result.get();
                    if (Integer.parseInt(enteredCode) == verificationCode) {
                        codeEnteredCorrectly = true;

                          Stage newStage = new Stage();
        
        // Create a new root node for the scene (AnchorPane)
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #00ACFF;");
        root.setPrefWidth(900);
        root.setPrefHeight(650);
        
        // Create and configure UI elements
        TextField newPswtf = new TextField();
        newPswtf.setLayoutX(293);
        newPswtf.setLayoutY(219);
        newPswtf.setPrefWidth(289);
        newPswtf.setPrefHeight(48);
        
        TextField cfNewPsw = new TextField();
        cfNewPsw.setLayoutX(293);
        cfNewPsw.setLayoutY(357);
        cfNewPsw.setPrefWidth(289);
        cfNewPsw.setPrefHeight(48);
        
        Label newPasswordLabel = new Label("New Password");
        newPasswordLabel.setLayoutX(21);
        newPasswordLabel.setLayoutY(215);
        newPasswordLabel.setPrefWidth(256);
        newPasswordLabel.setPrefHeight(56);
        newPasswordLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        newPasswordLabel.setFont(new Font(24));
        
        Label confirmNewPasswordLabel = new Label("Confirm New Password");
        confirmNewPasswordLabel.setLayoutX(21);
        confirmNewPasswordLabel.setLayoutY(353);
        confirmNewPasswordLabel.setPrefWidth(256);
        confirmNewPasswordLabel.setPrefHeight(56);
        confirmNewPasswordLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        confirmNewPasswordLabel.setFont(new Font(24));
        
        ImageView logoImageView = new ImageView(new Image(getClass().getResourceAsStream("../images/logo-topnet.png")));
        logoImageView.setLayoutX(281);
        logoImageView.setLayoutY(14);
        logoImageView.setFitWidth(385);
        logoImageView.setFitHeight(158);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);
        
        Button saveButton = new Button("Save");
        saveButton.setLayoutX(377);
        saveButton.setLayoutY(473);
        saveButton.setPrefWidth(122);
        saveButton.setPrefHeight(56);
        
        // Add the UI elements to the root node
        root.getChildren().addAll(newPswtf, cfNewPsw, newPasswordLabel,
                confirmNewPasswordLabel, logoImageView, saveButton);
        
        // Create a new scene with the root node
        Scene scene = new Scene(root);
        
        
        // Set the scene for the new stage
        newStage.setScene(scene);
        
        // Show the new stage to the user
        newStage.show();
        
        // Assuming you have a button that triggers the check
saveButton.setOnAction(e -> {
    String newPsws = newPswtf.getText();
    String cfNewPsws = cfNewPsw.getText();
    
    if (newPsws.equals(cfNewPsws)) {
      
        
        uc.changePassword(recov_mail.getText(), newPsws);
            try {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root1 = loader1.load();

        // Get the current stage
        Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new FXML file as the content of the stage
        stage1.setScene(new Scene(root1));
        stage1.show();
    } catch (IOException e1) {
        e1.printStackTrace();
    }
        
        
     } else {
        // Code to display a message indicating the content is not equal
        // Example: displaying an alert dialog
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("The password does not match.");
        alert.showAndWait();
    }
});

        
        
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect Verification Code");
                        alert.setHeaderText(null);
                        alert.setContentText("The entered verification code is incorrect.");
                        alert.showAndWait();
                    }
                } else {
                    // User canceled the input dialog
                    System.out.println("Verification code input canceled.");
                    break;
                }
            }
        } else {
            // User does not exist, show a message dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The user does not exist.");
            alert.showAndWait();
        }
    } else {
        // Invalid email format, show an error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Email");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a valid email address.");
        alert.showAndWait();
    }
}

private int send_mail(String mail, String name) {
    // Sender's credentials
    String senderEmail = "No_Reply_Topnet@outlook.com";
    String senderPassword = "root123456.";

    // Recipient's email
    String recipientEmail = mail;
    Random random = new Random();

    // Generate a random verification code
    int verificationCode = random.nextInt(90000) + 10000;

    // Email subject
    String subject = "Account Recovery - Verification Code";

    // Email body with HTML formatting
    String body = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "  <title>Account Recovery - Verification Code</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
            "    <tr>\n" +
            "      <td align=\"center\" bgcolor=\"#f5f5f5\" style=\"padding: 40px 0;\">\n" +
            "        <img src=\"https://drive.google.com/uc?export=download&id=1DQkHOnGAjxcTrAUUclFhLs1pLQ_Qhqjh\" alt=\"Company Logo\" width=\"200\" height=\"auto\">\n" +
            "      </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td bgcolor=\"#ffffff\" style=\"padding: 40px 30px;\">\n" +
            "        <h2>Account Recovery - Verification Code</h2>\n" +
            "        <p>Hello " + name + ",</p>\n" +
            "        <p>We have received a request to recover your account. Please use the following verification code to proceed with the account recovery:</p>\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top: 30px;\">\n" +
            "          <tr>\n" +
            "            <td align=\"center\" bgcolor=\"#007bff\" style=\"border-radius: 5px;\">\n" +
            "              <p style=\"display: inline-block; padding: 15px 30px; color: #ffffff; text-decoration: none; font-weight: bold;\">" + verificationCode + "</p>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </table>\n" +
            "        <p>If you did not request an account recovery, please disregard this email.</p>\n" +
            "      </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td bgcolor=\"#f5f5f5\" style=\"padding: 20px 30px;\">\n" +
            "        <p style=\"margin: 0;\">If you have any questions, feel free to <a href=\"mailto:support@example.com\">contact our support team</a>.</p>\n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </table>\n" +
            "</body>\n" +
            "</html>";

    // SMTP server configuration
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp-mail.outlook.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    // Create a session with the SMTP server
    Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmail, senderPassword);
        }
    });

    try {
        // Create a MimeMessage object
        MimeMessage message = new MimeMessage(session);

        // Set the sender and recipient addresses
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

        // Set the email subject and body
        message.setSubject(subject);
        message.setContent(body, "text/html");

        // Send the email
        Transport.send(message);

        System.out.println("Recovery verification email sent successfully.");
    } catch (MessagingException ex) {
        System.out.println("Failed to send recovery verification email. Error: " + ex.getMessage());
    }
    return verificationCode;
}

    @FXML
    private void back(MouseEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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
