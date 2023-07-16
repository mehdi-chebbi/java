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
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javafx.scene.Scene;
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
    @Override
public void initialize(URL url, ResourceBundle rb) {
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
    // Create a new stage for the new window
    Stage newStage = new Stage();

    // Create an AnchorPane as the root of the new window
    AnchorPane root = new AnchorPane();
    root.setPrefWidth(900);
    root.setPrefHeight(650);
    root.setStyle("-fx-background-color: #00ACFF;");

    // Create a TableView to display the filtered users
    TableView<user> tableView = new TableView<>();
    tableView.setLayoutX(13);
    tableView.setLayoutY(100);
    tableView.setPrefWidth(875);
    tableView.setPrefHeight(338);

    // Create the table columns
    TableColumn<user, String> firstNameColumn = new TableColumn<>("First Name");
    TableColumn<user, String> lastNameColumn = new TableColumn<>("Last Name");
    TableColumn<user, LocalDateTime> createdOnColumn = new TableColumn<>("Created On");
    TableColumn<user, LocalDateTime> modifiedOnColumn = new TableColumn<>("Modified On");
    TableColumn<user, String> createdByColumn = new TableColumn<>("Created By");
    TableColumn<user, String> modifiedByColumn = new TableColumn<>("Modified By");
    TableColumn<user, String> roleColumn = new TableColumn<>("Role");

    // Set up the column cell value factories
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
    modifiedOnColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedOn"));
    createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    modifiedByColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedBy"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

    // Add the columns to the table view
    tableView.getColumns().addAll(firstNameColumn, lastNameColumn, createdOnColumn, modifiedOnColumn, createdByColumn, modifiedByColumn, roleColumn);

    // Call the method to fetch user data
    UserCRUD userCRUD = new UserCRUD();
    List<user> userList = userCRUD.recuperer();

    // Filter out users with createdBy attribute as null
    List<user> filteredUserList = userList.stream()
            .filter(user -> user.getCreatedBy() == null)
            .collect(Collectors.toList());

    // Populate the table view with the filtered user data
    tableView.getItems().addAll(filteredUserList);

    // Add the table view to the root pane
    root.getChildren().add(tableView);

    // Add the logo ImageView
        ImageView logoImageView = new ImageView(new Image(getClass().getResourceAsStream("../images/logo-topnet.png")));
    logoImageView.setLayoutX(13);
    logoImageView.setLayoutY(-7);
    logoImageView.setFitWidth(271);
    logoImageView.setFitHeight(124);
    root.getChildren().add(logoImageView);

    // Create an Accept button
    Button acceptButton = new Button("Accept");
    acceptButton.setLayoutX(633);
    acceptButton.setLayoutY(588);
    acceptButton.setPrefWidth(100);
    acceptButton.setStyle("-fx-background-color: yellow;");
    root.getChildren().add(acceptButton);

    // Create a new scene with the root pane
    Scene scene = new Scene(root);

    // Set the scene for the new window and show it
    newStage.setScene(scene);
    newStage.show();
}

}