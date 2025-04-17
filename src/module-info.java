module JavaProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires java.desktop;
	
	
	opens application to javafx.graphics,javafx.fxml;
//	opens businessLogic to javafx.fxml;

    opens businessLogic to javafx.base, javafx.fxml;
	opens domain to javafx.base;
}