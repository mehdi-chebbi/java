/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.UserSession;
import entities.user;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.UserCRUD;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class Home_adminController implements Initializable {

    @FXML
    private TableColumn<user, String> fncolumn;
    @FXML
    private TableColumn<user, String> lnamecolumn;
    @FXML
    private TableColumn<user, String> concolumn;
    @FXML
    private TableColumn<user, String> lmodcolumn;
    @FXML
    private TableColumn<user, String> creabycolumn;
    @FXML
    private TableColumn<user, String> modbycolumn;
    @FXML
    private TableColumn<user, String> rolecolumn;
    @FXML
    private TableView<user> UserTableView;
    @FXML
    private TextField logintf;
    @FXML
    private TextField pswtf;
    @FXML
    private TextField roletf;
    @FXML
    private TextField fnametf;
    @FXML
    private Button modbtn;
    @FXML
    private Button deletebnt;
    @FXML
    private TextField lnametf; 
    
    UserSession session = UserSession.getInstance();
    @FXML
    private ImageView logoiv;
    @FXML
    private Button pending_button;
    UserSession userSession = UserSession.getInstance();
    @FXML
    private Hyperlink logout;

    @Override
public void initialize(URL url, ResourceBundle rb) {

// Check if a user is logged in

        fncolumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    lnamecolumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    concolumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
    lmodcolumn.setCellValueFactory(new PropertyValueFactory<>("modifiedOn"));
    creabycolumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    modbycolumn.setCellValueFactory(new PropertyValueFactory<>("modifiedBy"));
    rolecolumn.setCellValueFactory(new PropertyValueFactory<>("role"));

    // Call the method to fetch user data
    UserCRUD userCRUD = new UserCRUD();
    List<user> userList = userCRUD.recuperer();

    // Filter out users with createdBy attribute as null
    List<user> filteredUserList = userList.stream()
            .filter(user -> user.getCreatedBy() != null)
            .collect(Collectors.toList());

    // Populate the table view with the filtered user data
    UserTableView.getItems().addAll(filteredUserList);
}

    
    @FXML
    private void clicked(javafx.scene.input.MouseEvent event) { 
        int myIndex =UserTableView.getSelectionModel().getSelectedIndex();
        user selecteduser = UserTableView.getItems().get(myIndex);  
        System.out.println("Selected user: " + event.getTarget());

        logintf.setText(selecteduser.getLogin()); 
        pswtf.setText(selecteduser.getPassword()); 
        roletf.setText(selecteduser.getRole());
        fnametf.setText(selecteduser.getFirstName()); 
        lnametf.setText(selecteduser.getLastName()); 
    }

    @FXML
    private void modify(ActionEvent event) {
        int selectedIndex = UserTableView.getSelectionModel().getSelectedIndex();

    if (selectedIndex < 0) {
        // Display a warning dialog if no publication is selected
        Alert alert1 = new Alert(Alert.AlertType.WARNING);
        alert1.setTitle("Warning");
        alert1.setHeaderText("No user is selected !");
        alert1.setContentText("Please select a user to modify !");
        alert1.showAndWait();
    }else {
        user selecteduser = UserTableView.getItems().get(selectedIndex);

        if (logintf.getText().isEmpty() || pswtf.getText().isEmpty() || roletf.getText().isEmpty() || fnametf.getText().isEmpty() || lnametf.getText().isEmpty()) {
            Alert a2 = new Alert(Alert.AlertType.WARNING);
            a2.setTitle("Error");
            a2.setContentText("Please fill all fields !");
            a2.show();
        }else { 
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(currentTimeMillis);
            user u = new user(); 
            u.setLogin(logintf.getText());
            u.setPassword(pswtf.getText()); 
            u.setRole(roletf.getText()); 
            u.setFirstName(fnametf.getText()); 
            u.setLastName(lnametf.getText());
            u.setModifiedOn(timestamp);
            u.setIdUser(selecteduser.getIdUser()); 
            u.setModifiedBy(session.getLoggedInUser().getLogin());
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edit confirmation");
            alert.setHeaderText("Are you sure you want to edit this user ?");
            alert.setContentText("This action is irreversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                UserCRUD us = new UserCRUD();

                try {
                    us.editUser(u);
                     
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setTitle("User eddited");
                    al.setContentText("The user is modified successfully!");
                    al.show();
                } catch (SQLException ex) {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error");
                    al.setContentText("There is an Error edditing this user: " + ex.getMessage());
                    al.show();
                }
            }
        
    
    }
    } updateTableView(); clearfields(); 
    }

    @FXML
    private void delete(ActionEvent event) {
         int selectedIndex = UserTableView.getSelectionModel().getSelectedIndex();
    if (selectedIndex >= 0) { 
        user selectedUser = UserTableView.getItems().get(selectedIndex); 
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this user ?");
        alert.setContentText("This action is irreversible."); 
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) { // Check if OK button is clicked
            UserCRUD us = new UserCRUD();
            try {
                us.deleteUserById(selectedUser.getIdUser());
                 
                 Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setTitle("User deleted");
                al.setContentText("The user has been deleted successfully !!");
                al.show();
            } catch (SQLException ex) {
                System.out.println("Erreur d'accès à la BD");
            }
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No user is selected !");
        alert.setContentText("Please select a user to delete.");
        alert.showAndWait();
    } updateTableView(); clearfields();
    }
    public void updateTableView() {
        UserCRUD us = new UserCRUD();
    List<user> userList = us.recuperer();
    UserTableView.getItems().setAll(userList);
}
    public void clearfields(){
        logintf.clear();
        pswtf.clear();
        roletf.clear();
        fnametf.clear(); 
        lnametf.clear();
    
    }

@FXML
private void pending(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pending.fxml"));
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
    private void logoutfn(ActionEvent event) { 
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to log out ?");
        alert.setContentText("Please Confirm."); 
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        try { 
                    UserSession userSession = UserSession.getInstance(); 
                    userSession.setLoggedInUser(null);
             
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    ((Stage) logout.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
        System.out.println(UserSession.getInstance());
    }

} 
}