package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import domain.Table;

public class bookTableController {

	private Stage stage;
    private Scene scene;
    private Parent root;
    
	@FXML
    private DatePicker ReservationDatePicker;

    // TableView for displaying room details
    @FXML
    private TableView<Table> Table_table;

    @FXML
    private TableColumn<Table, Integer>  TableNo;

    @FXML
    private TableColumn<Table, String> Capacity;
    
    @FXML
    private TextField capacityField;

    @FXML
    private TextField starttimeField;
    
    @FXML
    private TextField endtimeField;
 
    
    @FXML
    private TextField tablenofield;

    @FXML
    public void initialize() {
    	TableNo.setCellValueFactory(new PropertyValueFactory<>("TableNo"));
    	Capacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
    }
    
    @FXML
    private void checkTableAvailability() {
        Table.checkTableAvailability(ReservationDatePicker, capacityField, starttimeField, endtimeField, Table_table);
    }

    @FXML
    public void clicknextAtables(ActionEvent event) throws IOException {
        Table.clickNextAtables(event, ReservationDatePicker, capacityField, starttimeField, endtimeField, tablenofield);
    }
    
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
       
    @FXML
    public void clickbookTableback(ActionEvent event) throws IOException
    {
       root = FXMLLoader.load(getClass().getResource("/application/guest.fxml"));
       stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       String css= this.getClass().getResource("/application/application.css").toExternalForm();
       scene.getStylesheets().add(css);
       stage.setScene(scene);
       stage.show();
    }
    
}
