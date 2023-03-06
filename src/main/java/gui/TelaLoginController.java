package gui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TelaLoginController implements Initializable{
	@FXML
	private TextField usuarioTextField;
	@FXML
	private Button btLogin;
	@FXML
	private PasswordField senhaPasswordField;

	@FXML
	private void fazerLogin() {
	    String usuario = usuarioTextField.getText();
	    String senha = senhaPasswordField.getText();

	    // Chamar o serviço de autenticação do Spring Boot para verificar o usuário e
	    // senha
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/usuarios?usuario={usuario}&senha={senha}";
	    Map<String, String> params = new HashMap<>();
	    params.put("usuario", usuario);
	    params.put("senha", senha);

	    try {
	        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class, params);

	        // Se o login for bem-sucedido, abrir a próxima tela
	        if (response.getStatusCode() == HttpStatus.OK) {
	            Map<String, Object> resultado = response.getBody();
	            Integer permissao = (Integer) resultado.get("permissao");
	            abrirTelaPermissao(permissao);
	        } else {
	            // Caso contrário, exibir uma mensagem de erro
	            Alert alerta = new Alert(Alert.AlertType.ERROR);
	            alerta.setTitle("Erro de login");
	            alerta.setContentText("Usuário ou senha incorretos");
	            alerta.showAndWait();
	        }
	    } catch (Exception e) {
	        // Exibir mensagem de erro para o usuário
	        Alert alerta = new Alert(Alert.AlertType.ERROR);
	        alerta.setTitle("Erro ao fazer login");
	        alerta.setContentText("Não foi possível fazer o login. Sem permissão de acesso ou sem conexão de internet.");
	        alerta.showAndWait();
	    }
	}


	private void abrirTelaPermissao(Integer permissao) {
		if (permissao == 1) {
			try {
				Parent parent = FXMLLoader.load(getClass().getResource("ViewRemoto.fxml"));
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (permissao == 2) {
			try {
				Parent parent = FXMLLoader.load(getClass().getResource("ViewRemotoPedido.fxml"));
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (permissao == 3) {
			try {
				Parent parent = FXMLLoader.load(getClass().getResource("ViewRemotoDados.fxml"));
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			// Exibir uma mensagem de erro caso o número da permissão seja inválido
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Erro de permissão");
			alerta.setContentText("Número de permissão inválido: " + permissao);
			alerta.showAndWait();
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
