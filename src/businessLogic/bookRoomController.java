package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import domain.Room;

public class bookRoomController {
    
	private Stage stage;
    private Scene scene;
    private Parent root;
    
	@FXML
    private DatePicker checkinDatePicker,checkoutDatePicker;

    // TableView for displaying room details
    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer>  roomNoColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    @FXML
    private TableColumn<Room, Integer> priceColumn;
    
    // TextField for entering search criteria (e.g., guest name or room number)  
    @FXML
    private TextField roomnofield;
    
    @FXML
    public void initialize() {
    	  roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
    	    roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
    	    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    	    if (roomnofield == null) {
    	        System.out.println("roomnofield is null");
    	    } else {
    	        System.out.println("roomnofield is initialized");
    	    }
    }
    
    @FXML
    private void checkRoomAvailability() {
        // Delegate to the Room class
        Room.checkRoomAvailability(checkinDatePicker, checkoutDatePicker, roomTable);
    }

    @FXML
    public void clicknextArooms(ActionEvent event) throws IOException {
        // Delegate to the Room class
        String checkinDate = checkinDatePicker.getValue().toString();
        String checkoutDate = checkoutDatePicker.getValue().toString();
        Room.clickNextARooms(event, checkinDate, checkoutDate, roomnofield.getText(), roomTable);
    }
    
    @FXML
    public void switchtoback(ActionEvent event) throws IOException
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
