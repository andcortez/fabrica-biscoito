package gui.model;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class Restricao {
	
	// método para restringir que o usuario coloque letra nos campos de ingredientes da interface
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(oldValue);
			}
		});
	}
	
	// os 2 métodos abaixos são pop-up de informação e alerta para o usuario em determinadas condições
	public static void showInformation(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		PauseTransition delay = new PauseTransition(Duration.seconds(1));
		delay.setOnFinished(e -> alert.hide());
		alert.show();
		delay.play();
	}
	
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
}
