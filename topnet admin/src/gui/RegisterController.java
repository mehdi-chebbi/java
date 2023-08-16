/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.user;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import services.UserCRUD;



/**
 * FXML Controller class
 *
 * @author DELL
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField nom_tf;
    @FXML
    private TextField prenom_tf;
    @FXML
    private TextField mail_tf;
    @FXML
    private TextField confirm_pass_tf;
    @FXML
    private TextField password_tf;
    @FXML
    private Button add_new_user_button;
    @FXML
    private Button back_button;
        UserCRUD CRUD = new UserCRUD();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 password_tf.textProperty().addListener((observable, oldValue, newValue) -> {
        checkPasswordMatch();
    });

    confirm_pass_tf.textProperty().addListener((observable, oldValue, newValue) -> {
        checkPasswordMatch();
    });    }    
    
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

@FXML
private Label confirmPassLabel;
    @FXML
  private void add_new_user(ActionEvent event) {
    String nom = nom_tf.getText();
    String prenom = prenom_tf.getText();
    String mail = mail_tf.getText();
    String password = password_tf.getText();
    String confirmPass = confirm_pass_tf.getText();

    if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
        // At least one field is empty, display error messages
        StringBuilder errorMessage = new StringBuilder();
        if (nom.isEmpty()) {
            errorMessage.append("Nom field is empty\n");
        }        
        if (prenom.isEmpty()) {
            errorMessage.append("Prenom field is empty\n");
        }
        if (mail.isEmpty()) {
            errorMessage.append("Mail field is empty\n");
        }if (!isValidEmail(mail)) {
    errorMessage.append("Invalid email address\n");
}
        if (password.isEmpty()) {
            errorMessage.append("Password field is empty\n");
        }
         if (password.length() < 8) {
    errorMessage.append("Password should be at least 8 characters long\n");
}
        if (confirmPass.isEmpty()) {
            errorMessage.append("Confirm Password field is empty\n");
        }

        // Create a new window to show error messages
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty Fields");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
        return;
    }
    
      boolean userExists = CRUD.checkUserExists(mail);
    if (userExists) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("User Already Exists");
        alert.setContentText("A user with the given email already exists. Please choose a different email.");
        alert.showAndWait();
        return;
    }
boolean isValidEmail = isValidEmail(mail);
if (!isValidEmail) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Invalid Email");
    alert.setContentText("The email address entered is not valid. Please enter a valid email address.");
    alert.showAndWait();
    return;
}
    // Open a new window to prompt for a code
    TextInputDialog codeDialog = new TextInputDialog();
    codeDialog.setTitle("Enter Code");
    codeDialog.setHeaderText("Enter the code to proceed");
    codeDialog.setContentText("Code:");
    int code=send_mail(mail, nom);
    Optional<String> codeResult = codeDialog.showAndWait();
    int enteredCode = Integer.parseInt(codeResult.orElse(""));


    // Check if the code matches the expected value
    if (codeResult.isPresent() && enteredCode== code) {
        // Code is correct, proceed with adding the user

        // Retrieve the current system time
        long currentTimeMillis = System.currentTimeMillis();

        // Send email

        // Create a Timestamp object using the current time

        // Create a new user instance and set the properties
        user new_user = new user();
        new_user.setFirstName(prenom);
        new_user.setLastName(nom);
        new_user.setLogin(mail);
        new_user.setPassword(password);
        new_user.setRole("Utilisateur"); 
        new_user.setCreatedBy(mail); 
        new_user.setModifiedBy("not_modified");

        // Add the user
        UserCRUD CRUD = new UserCRUD();
        CRUD.addUser(new_user);
             openRegistrationCompletionScene();

        
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
        
        
        
        
    } else {
        // Code is incorrect, display an error message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Code");
        alert.setContentText("The entered code is invalid. User not added.");
        alert.showAndWait();
    }
}

    private void checkPasswordMatch() {
    String password = password_tf.getText();
    String confirmPass = confirm_pass_tf.getText();

    if (password.isEmpty() || confirmPass.isEmpty()) {
        // If either password or confirm password field is empty, clear the label
        confirmPassLabel.setText("");
    } else if (password.equals(confirmPass)) {
        // Passwords match
        confirmPassLabel.setText("Passwords match");
        confirmPassLabel.setTextFill(Color.GREEN);
    } else {
        // Passwords don't match
        confirmPassLabel.setText("Passwords don't match");
        confirmPassLabel.setTextFill(Color.RED);
    }
    
    
    

}
    
    private void openRegistrationCompletionScene() {
    try {
        // Create a new stage for the registration completion scene
        Stage completionStage = new Stage();
        
        // Create a label with the registration completion message
        Label messageLabel = new Label("Registration completed. Waiting for admin approval.");
        
        // Create a VBox to hold the message label
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(messageLabel);
        
        // Create a new scene with the VBox
        Scene completionScene = new Scene(vbox, 400, 200);
        
        // Set the scene to the completion stage
        completionStage.setScene(completionScene);
        completionStage.setTitle("Registration Completed");
        
        // Show the completion stage
        completionStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
   private int send_mail(String mail, String Name) {
    // Sender's credentials
    String senderEmail = "No_Reply_Topnet@outlook.com";
    String senderPassword = "root123456.";

    // Recipient's email
    String recipientEmail = mail;
    Random random = new Random();

    // Generate a random integer with five digits
    int randomNumber = random.nextInt(90000) + 10000;
    
    // Email subject
    String subject = "Confirmation Email";

    // Email body with HTML formatting
// Email body with HTML formatting
String body = "<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"  <meta charset=\"UTF-8\">\n" +
"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"  <title>Email Confirmation</title>\n" +
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
"        <h2>Thank you for your registration!</h2>\n" +
"        <p>Hello " + Name + ",</p>\n" +
"        <p>We are excited to have you on board. Please use the following code to confirm your email address:</p>\n" +
"        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top: 30px;\">\n" +
"          <tr>\n" +
"            <td align=\"center\" bgcolor=\"#f5f5f5\" style=\"border: 1px solid #007bff; border-radius: 5px; padding: 15px 30px;\">\n" +
"              <span style=\"font-weight: bold; font-size: 16px;\">" + randomNumber + "</span>\n" +
"            </td>\n" +
"          </tr>\n" +
"        </table>\n" +
"        <p>If you did not sign up for our service, please disregard this email.</p>\n" +
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

        System.out.println("Confirmation email sent successfully.");
    } catch (MessagingException ex) {
        System.out.println("Failed to send confirmation email. Error: " + ex.getMessage());
    }
    return randomNumber;
}

    @FXML
    private void back_action(ActionEvent event) {
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
