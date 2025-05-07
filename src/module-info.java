module OX {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires com.zaxxer.hikari;
	requires java.sql;
	requires org.slf4j;
	requires javafx.base;
    requires javafx.graphics;
    opens aplication.model to javafx.base;
    exports aplication;
    exports aplication.model; 
	
	opens aplication to javafx.graphics, javafx.fxml;
}
