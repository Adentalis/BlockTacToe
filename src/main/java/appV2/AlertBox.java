package appV2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {
	
	public Alert alert;

	
	public AlertBox(String title, String message) {
		alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public AlertBox() {
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("BlockTacToe");
		alert.setHeaderText(null);
		alert.setContentText("Transaktion in Bearbeitung. Bitte warten!");
		alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
		alert.show();
	}
	
	public void close() {
		alert.close();
	}
}
