/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Khalil
 */
public class ForgotpswController implements Initializable {

    @FXML
    private TextField newpswtf;
    @FXML
    private TextField cfnewpsw;
    @FXML
    private Button savebtn;
    private String mail;

    public void setMail(String mail) {
        this.mail = mail;
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.print(mail);
    }    

    @FXML
    private void save(ActionEvent event) {
         String newPassword = newpswtf.getText();
    String confirmPassword = cfnewpsw.getText();

    if (newPassword.equals(confirmPassword)) {
         
        
        
    } else {
        
        
        
    }
        
        
        
        
    }
    
}
