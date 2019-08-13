package base;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMsg {

	public static String WARNING = "WARNING";
	public static String ERROR = "ERROR";
	public static String INFO = "INFO";
        public Alert alert;
	
	public void alertMsg(AlertType alertType, String title, String headerText, String contetText) {
		
		alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contetText);
		if(alertType.compareTo(AlertType.ERROR) == 0) {
			alert.getDialogPane().setPrefSize(400, 150);
			alert.setResizable(true);
		}
		alert.showAndWait();
                
	}
        
}
