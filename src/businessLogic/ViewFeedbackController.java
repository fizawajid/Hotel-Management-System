package businessLogic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Ensure this is imported
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import domain.Feedback;
import database.dbHandler;

public class ViewFeedbackController {
    private dbHandler dbHandler; // Instance of database handler

    @FXML
    private TableColumn<Feedback, Integer> idColumn;

    @FXML
    private TableColumn<Feedback, Integer> guestIdColumn;

    @FXML
    private TableColumn<Feedback, String> serviceColumn;

    @FXML
    private TableColumn<Feedback, Integer> ratingColumn;

    @FXML
    private TableView<Feedback> feedbacktable;

    Controller1 control = new Controller1();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Fetch data from database and populate table
        if (dbHandler == null) {
            dbHandler = new dbHandler();
        }
        ObservableList<Feedback> feedbackData = dbHandler.loadFeedbackData(database.dbHandler.getLoggedInUserRole());
        feedbacktable.setItems(feedbackData);
    }

    @FXML
    public void switchtoback(ActionEvent event) throws IOException {
        String role = database.dbHandler.getLoggedInUserRole(); // Get the logged-in user's role
        String fxmlFile = null;

        // Determine the target page based on the role
        switch (role) {
        
            case "crew":
                fxmlFile = "/application/crew.fxml";
                break;
            case "gym manager":
                fxmlFile = "/application/gymManager.fxml";
                break;
            case "manager":
                fxmlFile = "/application/mainManager.fxml";
                break;
            case "restaurant staff":
                fxmlFile = "/application/restaurantStaff.fxml";
                break;
            default:
                control.showAlert(AlertType.ERROR, "Error", "Role not recognized.");
                return; // Exit if role is not recognized     
        }

        // Load the appropriate FXML file
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}
