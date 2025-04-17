package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class RestaurantController extends StaffController {

    @FXML
    public void switchToHandleRestaurantServices(ActionEvent event) throws IOException {
        loadScene(event, "/application/handleRestaurantService.fxml");
    }
}