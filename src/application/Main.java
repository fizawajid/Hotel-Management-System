package application;

import database.dbHandler;
import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application{
	

//    @FXML
//    private void connectToDatabase() {
//        if (dbHandler.getConnection() != null) {
//            System.out.println("Connected to Database!");
//        } else {
//        	System.out.println("Failed to connect!");
//        }
//    	}
		@Override
	    public void start(Stage stage) {
			try {
				
				Parent root=FXMLLoader.load(getClass().getResource("main.fxml"));
				Scene scene= new Scene(root);
				String css=this.getClass().getResource("application.css").toExternalForm();
				scene.getStylesheets().add(css);
				stage.setScene(scene);
				stage.setTitle("Stellar Stays");
				stage.show();
			} catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		
		
	    
		
		@Override
		public void stop() {
			DatabaseManager.closeConnection();
		}

	    public static void main(String[] args) {
	        launch(args);
	    }
}