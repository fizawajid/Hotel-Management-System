package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class CrewController extends StaffController {

    @FXML
    public void switchToManageRooms(ActionEvent event) throws IOException {
        loadScene(event, "/application/manageRooms.fxml");
    }
}