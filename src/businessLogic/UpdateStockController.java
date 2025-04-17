package businessLogic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import database.dbHandler;
import domain.StockReport;

public class UpdateStockController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private dbHandler dbHandler;
    
    public UpdateStockController() {
        this.dbHandler = new dbHandler(); // Ensure this initialization occurs
    }

    @FXML
    private TextField serviceIdField,serviceIdField2,serviceIdField3;

    @FXML
    private TableColumn<StockReport, Integer> idColumn,idColumn2,idColumn3;
    
    @FXML
    private TableColumn<StockReport, String> handlerColumn,handlerColumn2,handlerColumn3;

    @FXML
    private TableColumn<StockReport, String> inventoryColumn,inventoryColumn2,inventoryColumn3;

    @FXML
    private TableColumn<StockReport, Integer> quantityColumn,quantityColumn2,quantityColumn3;
    
    @FXML
    private TableView<StockReport> gymInventoryTable,gymInventoryTable2,gymInventoryTable3;
    
    //@FXML
    private String inventoryType;
    
    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
        System.out.println(inventoryType);
    }
    
    public String getInventoryType() {
       return inventoryType;
        
    }
    
    public void setupInventory() {
        // Check if inventoryType is set
        if (inventoryType != null) {
        	
            // Populate the table based on inventoryType
            if ("gym manager".equals(inventoryType)) {
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                handlerColumn.setCellValueFactory(new PropertyValueFactory<>("Handler"));
                inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("Inventory"));
                quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                ObservableList<StockReport> feedbackData1 = dbHandler.displayStockReport(inventoryType);
                gymInventoryTable.setItems(feedbackData1);
            } else if ("restaurant staff".equals(inventoryType)) {
                idColumn2.setCellValueFactory(new PropertyValueFactory<>("id"));
                handlerColumn2.setCellValueFactory(new PropertyValueFactory<>("Handler"));
                inventoryColumn2.setCellValueFactory(new PropertyValueFactory<>("Inventory"));
                quantityColumn2.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                ObservableList<StockReport> feedbackData2 = dbHandler.displayStockReport(inventoryType);
                gymInventoryTable2.setItems(feedbackData2);
            }
            else if ("crew".equals(inventoryType)) {
                idColumn3.setCellValueFactory(new PropertyValueFactory<>("id"));
                handlerColumn3.setCellValueFactory(new PropertyValueFactory<>("Handler"));
                inventoryColumn3.setCellValueFactory(new PropertyValueFactory<>("Inventory"));
                quantityColumn3.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                ObservableList<StockReport> feedbackData3 = dbHandler.displayStockReport(inventoryType);
                gymInventoryTable3.setItems(feedbackData3);
            }
        }
    }
    
    @FXML
    public void backupdateinventory(ActionEvent event) throws IOException {
    	 root = FXMLLoader.load(getClass().getResource("/application/Inventory.fxml"));
    	 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	 scene = new Scene(root);
         String css= this.getClass().getResource("/application/application.css").toExternalForm();
         scene.getStylesheets().add(css);
    	 stage.setScene(scene);
    	 stage.show();
    }
    
    @FXML
    public void backupdateinventoryR(ActionEvent event) throws IOException {
    	 root = FXMLLoader.load(getClass().getResource("/application/Inventory.fxml"));
    	 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	 scene = new Scene(root);
         String css= this.getClass().getResource("/application/application.css").toExternalForm();
         scene.getStylesheets().add(css);
    	 stage.setScene(scene);
    	 stage.show();
    }
    
    
    @FXML
    public void backupdateinventory_RO(ActionEvent event) throws IOException {
    	 root = FXMLLoader.load(getClass().getResource("/application/Inventory.fxml"));
    	 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	 scene = new Scene(root);
         String css= this.getClass().getResource("/application/application.css").toExternalForm();
         scene.getStylesheets().add(css);
    	 stage.setScene(scene);
    	 stage.show();
    }

	    @FXML
	    public void clickdone_i(ActionEvent event) throws IOException {
	    	 root = FXMLLoader.load(getClass().getResource("/application/mainManager.fxml"));
	    	 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	 scene = new Scene(root);
	         String css= this.getClass().getResource("/application/application.css").toExternalForm();
	         scene.getStylesheets().add(css);
	    	 stage.setScene(scene);
	    	 stage.show();
	    }
	    
	    @FXML
	    public void provideservice_g(ActionEvent event) throws IOException, SQLException {
	    	  String serviceIdText = serviceIdField.getText();

	    	    if (serviceIdText.isEmpty()) {
	    	        // Show an alert if the TextField is empty
	    	        showAlert("Error", "Service ID is required!", AlertType.ERROR);
	    	        return;
	    	    }
	    	    int serviceId;
	    	    try {
	    	        serviceId = Integer.parseInt(serviceIdText);
	    	        // Verify if the entered service ID exists in the database
	    	        if (!database.dbHandler.isStockIdValid(serviceId, "gym manager")) {
	    	        	showAlert("Error", "No service found with the provided ID.", AlertType.ERROR);
	    	            return;
	    	        }

	    	        if (database.dbHandler.removeStockReportById(serviceId, "gym manager")) {
	    	            // Refresh TableView
	    	            ObservableList<StockReport> updatedData = dbHandler.displayStockReport("gym manager");
	    	            gymInventoryTable.setItems(updatedData);

	    	            showAlert("Success", "Service ID " + serviceId + " has been removed successfully!", AlertType.INFORMATION);
	    	        } else {
	    	 
	    	            showAlert("Error", "No record found with Service ID " + serviceId, AlertType.ERROR);
	    	        }
	    	    } catch (NumberFormatException e) {
	    	        showAlert("Error", "Service ID must be a valid number!", AlertType.ERROR);
	    	    }
	    }
	    @FXML
	    public void provideservice_r(ActionEvent event) throws IOException, SQLException {
	    	  String serviceIdText = serviceIdField3.getText();
	    	  if(serviceIdText==null)
	    	  {
	    		  System.out.println("none");
	    	  }
	    	  
	    	    if (serviceIdText.isEmpty()) {
	    	        // Show an alert if the TextField is empty
	    	        showAlert("Error", "Service ID is required!", AlertType.ERROR);
	    	        return;
	    	    }
	    	    int serviceId;
	    	    try {
	    	        serviceId = Integer.parseInt(serviceIdText);
	    	        // Verify if the entered service ID exists in the database
	    	        if (!database.dbHandler.isStockIdValid(serviceId, "crew")) {
	    	        	showAlert("Error", "No service found with the provided ID.", AlertType.ERROR);
	    	            return;
	    	        }


	    	        if (database.dbHandler.removeStockReportById(serviceId, "crew")) {
	    	            // Refresh TableView
	    	            ObservableList<StockReport> updatedData = dbHandler.displayStockReport("crew");
	    	            gymInventoryTable3.setItems(updatedData);

	    	            showAlert("Success", "Service ID " + serviceId + " has been removed successfully!", AlertType.INFORMATION);
	    	        } else {
	    	 
	    	            showAlert("Error", "No record found with Service ID " + serviceId, AlertType.ERROR);
	    	        }
	    	    } catch (NumberFormatException e) {
	    	        showAlert("Error", "Service ID must be a valid number!", AlertType.ERROR);
	    	    }
	    }  
	    
	    @FXML
	    public void provideservice_res(ActionEvent event) throws IOException, SQLException {
	    	  String serviceIdText = serviceIdField2.getText();
	    	  if(serviceIdText==null)
	    	  {
	    		  System.out.println("none");
	    	  }
	    	  
	    	    if (serviceIdText.isEmpty()) {
	    	        // Show an alert if the TextField is empty
	    	        showAlert("Error", "Service ID is required!", AlertType.ERROR);
	    	        return;
	    	    }
	    	    int serviceId;
	    	    try {
	    	        serviceId = Integer.parseInt(serviceIdText);
	    	        // Verify if the entered service ID exists in the database
	    	        if (!database.dbHandler.isStockIdValid(serviceId, "restaurant staff")) {
	    	        	showAlert("Error", "No service found with the provided ID.", AlertType.ERROR);
	    	            return;
	    	        }


	    	        if (database.dbHandler.removeStockReportById(serviceId, "restaurant staff")) {
	    	            // Refresh TableView
	    	            ObservableList<StockReport> updatedData = dbHandler.displayStockReport("restaurant staff");
	    	            gymInventoryTable2.setItems(updatedData);

	    	            showAlert("Success", "Service ID " + serviceId + " has been removed successfully!", AlertType.INFORMATION);
	    	        } else {
	    	 
	    	            showAlert("Error", "No record found with Service ID " + serviceId, AlertType.ERROR);
	    	        }
	    	    } catch (NumberFormatException e) {
	    	        showAlert("Error", "Service ID must be a valid number!", AlertType.ERROR);
	    	    }
	    }
	    private void showAlert(String title, String message, AlertType alertType) {
	        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
		
}
