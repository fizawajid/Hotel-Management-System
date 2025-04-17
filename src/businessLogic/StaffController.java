package businessLogic;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class StaffController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    // Switch to feedback view
    @FXML
    public void switchToViewFeedback(ActionEvent event) throws IOException {
        loadScene(event, "/application/viewFeedback.fxml");
    }

    // Switch to stock report view
    @FXML
    public void switchToReportStock(ActionEvent event) throws IOException {
        loadScene(event, "/application/reportStock.fxml");
    }

    // Switch to staff menu
    @FXML
    public void switchToBack(ActionEvent event) throws IOException {
        Controller1 control = new Controller1();
        control.switchtostaff(event);
    }

    // Helper method to load and switch scenes
    protected void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}