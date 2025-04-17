package businessLogic;

import database.dbHandler;
import domain.RoomPayment;
import domain.TablePayment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ViewBillController {

    @FXML
    private Pagination pagination;

    @FXML
    private AnchorPane anchorPane; // Optional: In case additional layout changes are needed.

    // Called after FXML is loaded
    public void initialize() {
    	if (pagination == null) {
            System.out.println("Pagination is null! Check FXML and controller linkage.");
        } else {
            setupPagination();
        }
    }

    private void setupPagination() {
        pagination.setPageCount(2); // Two pages for RoomPayment and TablePayment

        pagination.setPageFactory(pageIndex -> {
            switch (pageIndex) {
                case 0:
                    return createRoomPaymentsTable();
                case 1:
                    return createTablePaymentsTable();
                default:
                    return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
	private TableView<RoomPayment> createRoomPaymentsTable() {
        TableView<RoomPayment> tableView = new TableView<>();
        List<RoomPayment> roomPayments = dbHandler.fetchRoomPayments();

        // Columns
        TableColumn<RoomPayment, Integer> roomNoColumn = new TableColumn<>("Room No");
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));

        TableColumn<RoomPayment, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<RoomPayment, Integer> guestIdColumn = new TableColumn<>("Guest ID");
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));

        TableColumn<RoomPayment, Integer> roomPriceColumn = new TableColumn<>("Room Price");
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));

        TableColumn<RoomPayment, Integer> foodPriceColumn = new TableColumn<>("Food Price");
        foodPriceColumn.setCellValueFactory(new PropertyValueFactory<>("foodPrice"));

        TableColumn<RoomPayment, String> paymentDateColumn = new TableColumn<>("Payment Date");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Add columns to table
        tableView.getColumns().addAll(roomNoColumn, amountColumn, guestIdColumn, roomPriceColumn, foodPriceColumn, paymentDateColumn);

        // Add data to table
        tableView.getItems().addAll(roomPayments);

        return tableView;
    }

    @SuppressWarnings("unchecked")
	private TableView<TablePayment> createTablePaymentsTable() {
        TableView<TablePayment> tableView = new TableView<>();
        List<TablePayment> tablePayments = dbHandler.fetchTablePayments();

        // Columns
        TableColumn<TablePayment, Integer> tableNoColumn = new TableColumn<>("Table No");
        tableNoColumn.setCellValueFactory(new PropertyValueFactory<>("tableNo"));

        TableColumn<TablePayment, Integer> guestIdColumn = new TableColumn<>("Guest ID");
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));

        TableColumn<TablePayment, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<TablePayment, String> paymentDateColumn = new TableColumn<>("Payment Date");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Add columns to table
        tableView.getColumns().addAll(tableNoColumn, guestIdColumn, amountColumn, paymentDateColumn);

        // Add data to table
        tableView.getItems().addAll(tablePayments);

        return tableView;
    }
    
    public void switchtoback(ActionEvent event)throws IOException
    {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/mainManager.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

}