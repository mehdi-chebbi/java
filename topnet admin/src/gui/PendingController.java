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
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UserCRUD;

/**
 * FXML Controller class
 *
 * @author Khalil
 */
public class PendingController implements Initializable {
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
    private ImageView logoiv;
    @FXML
    private Button accept_button;
    UserSession userSession = UserSession.getInstance();
    @FXML
    private Button delete_button;
    @FXML
    private ImageView backk;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       

        fncolumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    lnamecolumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    concolumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
    lmodcolumn.setCellValueFactory(new PropertyValueFactory<>("modifiedOn"));
    creabycolumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    modbycolumn.setCellValueFactory(new PropertyValueFactory<>("modifiedBy"));
    rolecolumn.setCellValueFactory(new PropertyValueFactory<>("role"));

    UserCRUD userCRUD = new UserCRUD();
    List<user> userList = userCRUD.recuperer();

    List<user> filteredUserList = userList.stream()
            .filter(user -> user.getCreatedBy() == null)
            .collect(Collectors.toList());
    

    UserTableView.getItems().addAll(filteredUserList);
    }    

     @FXML
    private void clicked(javafx.scene.input.MouseEvent event) { 
        int myIndex =UserTableView.getSelectionModel().getSelectedIndex();
        user selecteduser = UserTableView.getItems().get(myIndex);  
        System.out.println("Selected user: " + event.getTarget());

      
    }

   @FXML
private void Accept(ActionEvent event) {
    user loggedInUser = userSession.getLoggedInUser();
    user selectedUser = UserTableView.getSelectionModel().getSelectedItem();

    if (selectedUser != null) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Confirm Acceptance");
        confirmationAlert.setContentText("Are you sure you want to accept this user?");
        
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Update the selected user's createdBy value in the TableView
                selectedUser.setCreatedBy(loggedInUser.getLogin());
                
                // Update the TableView to reflect the changes
                int selectedIndex = UserTableView.getSelectionModel().getSelectedIndex();
                UserTableView.getItems().set(selectedIndex, selectedUser);
                
                // Update the selected user's createdBy value in the database
                UserCRUD userCRUD = new UserCRUD();
                userCRUD.updateCreatedBy(selectedUser.getIdUser(), loggedInUser.getLogin()); // Handle any SQL exceptions that might occur during the update

                // Display a success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setContentText("User accepted successfully!");
                successAlert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(PendingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    updateTableView(); 
}

    @FXML
    private void delete2(ActionEvent event) {
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
    } updateTableView(); 
    }
    public void updateTableView() {
        UserCRUD us = new UserCRUD();
    List<user> userList = us.recuperer();
    UserTableView.getItems().setAll(userList);
}

    @FXML
    private void back(MouseEvent event) { 
         try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_admin.fxml"));
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
    
