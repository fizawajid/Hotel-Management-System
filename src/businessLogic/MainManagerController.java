package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainManagerController {
	  	private Stage stage;
	    private Scene scene;
	    private Parent root;


	    @FXML
	    public void clickUpdateInventory(ActionEvent event) throws IOException
	    {	    	
	    	 root = FXMLLoader.load(getClass().getResource("/application/Inventory.fxml"));
	         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	         scene = new Scene(root);
	         String css= this.getClass().getResource("/application/application.css").toExternalForm();
	         scene.getStylesheets().add(css);
	         stage.setScene(scene);
	         stage.show();
	    }
	    @FXML
	    public void switchtoback(ActionEvent event) throws IOException
	    {
		 Controller1 control = new Controller1();
	     control.switchtostaff(event);
	    }
	    
	    @FXML
	    public void clickGyminventory(ActionEvent event) throws IOException
	    {
	    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/GymInventory.fxml")); // Load UpdateStock.fxml
	    	    Parent root = loader.load();

	    	    // Get the controller and set the context
	    	    UpdateStockController controller = loader.getController();
	    	    controller.setInventoryType("gym manager");
	    	    
	    	    controller.setupInventory();

	    	    // Show the scene
	    	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	    scene = new Scene(root);
	            String css= this.getClass().getResource("/application/application.css").toExternalForm();
	            scene.getStylesheets().add(css);
	    	    stage.setScene(scene);
	    	    stage.show();
	    }
	    	    
	    @FXML
	    public void clickroominventory(ActionEvent event) throws IOException
	    {	
	    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/RoomInventory.fxml")); // Load UpdateStock.fxml
	    	    Parent root = loader.load();

	    	    // Get the controller and set the context
	    	    UpdateStockController controller = loader.getController();
	    	    controller.setInventoryType("crew");
	    	    
	    	    controller.setupInventory();

	    	    // Show the scene
	    	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	    scene = new Scene(root);
	            String css= this.getClass().getResource("/application/application.css").toExternalForm();
	            scene.getStylesheets().add(css);
	    	    stage.setScene(scene);
	    	    stage.show();
	    } 
	    
	    @FXML
	    public void clickRestaurantinventory(ActionEvent event) throws IOException
	    {
	    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/RestaurantInventory.fxml")); // Load UpdateStock.fxml
	    	    Parent root = loader.load();

	    	    // Get the controller and set the context
	    	    UpdateStockController controller = loader.getController();
	    	    controller.setInventoryType("restaurant staff");
	    	    
	    	    controller.setupInventory();

	    	    // Show the scene
	    	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	    scene = new Scene(root);
	            String css= this.getClass().getResource("/application/application.css").toExternalForm();
	            scene.getStylesheets().add(css);
	    	    stage.setScene(scene);
	    	    stage.show();
	    }

	    @FXML
	    public void gobackinventory(ActionEvent event) throws IOException
	    {
	     root = FXMLLoader.load(getClass().getResource("/application/mainManager.fxml"));
	     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	     scene = new Scene(root);
	        String css= this.getClass().getResource("/application/application.css").toExternalForm();
	        scene.getStylesheets().add(css);
	     stage.setScene(scene);
	     stage.show();
	    }
	    
		@FXML
		public void ClickFeedbackViewM(ActionEvent event) throws IOException
		{
		 root = FXMLLoader.load(getClass().getResource("/application/ViewFeedback.fxml"));
		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 scene = new Scene(root);
	        String css= this.getClass().getResource("/application/application.css").toExternalForm();
	        scene.getStylesheets().add(css);
		 stage.setScene(scene);
		 stage.show();
		}
		@FXML
		public void ClickViewBills(ActionEvent event) throws IOException
		{
		 root = FXMLLoader.load(getClass().getResource("/application/viewBill.fxml"));
		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 scene = new Scene(root);
	        String css= this.getClass().getResource("/application/application.css").toExternalForm();
	        scene.getStylesheets().add(css);
		 stage.setScene(scene);
		 stage.show();
		}
}
