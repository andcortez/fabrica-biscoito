package gui;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Estoque;
import model.Pedido;
import model.Usuario;
import service.ThreadClientePedido;

public class ViewClientePedido implements Initializable {

	@FXML
	private ChoiceBox<String> tipoBiscoito;
	@FXML
	private TextField nomeUsuario;
	@FXML
	private TextField txtIngrediente1;
	@FXML
	private TextField txtIngrediente2;
	@FXML
	private TextField txtIngrediente3;
	@FXML
	private Button btConectar;
	@FXML
	private Label estoqueIngrediente1;
	@FXML
	private Label estoqueIngrediente2;
	@FXML
	private Label estoqueIngrediente3;
	
	@FXML
	public void atualizarLabelsEstoque() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/estoque")).GET()
					.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Gson gson = new Gson(); // Biblioteca GSON para conversão de JSON para objetos Java
			Estoque estoque = gson.fromJson(response.body(), Estoque.class);

			// Atualiza as labels com as informações de estoque
			estoqueIngrediente1.setText(Double.toString(estoque.getIngrediente1()));
			estoqueIngrediente2.setText(Double.toString(estoque.getIngrediente2()));
			estoqueIngrediente3.setText(Double.toString(estoque.getIngrediente3()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onBtConectar() {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/estoque")).GET()
					.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Gson gson = new Gson();
			Estoque estoque = gson.fromJson(response.body(), Estoque.class);

			Random rand = new Random();
			int randomNum = rand.nextInt(1000);

			// Cria o objeto Pedido
			Pedido pedido = new Pedido(tipoBiscoito.getSelectionModel().getSelectedItem(),
					Double.parseDouble(txtIngrediente1.getText()), Double.parseDouble(txtIngrediente2.getText()),
					Double.parseDouble(txtIngrediente3.getText()), randomNum);
			// Cria o objeto Usuario
			Usuario usuario = new Usuario(nomeUsuario.getText(), randomNum);

			if (estoque.verificarEstoqueSuficiente(pedido.getIngredienteA(), pedido.getIngredienteB(),
					pedido.getIngredienteC())) {
				String jsonPedido = gson.toJson(pedido);
				// Converte o objeto Usuario para JSON
				Gson gsonUser = new Gson();
				String jsonUsuario = gsonUser.toJson(usuario);

				// Cria a requisição POST do Pedido
				HttpPost requestPost = new HttpPost("http://localhost:8080/pedidos");
				StringEntity params = new StringEntity(jsonPedido);
				requestPost.addHeader("content-type", "application/json");
				requestPost.setEntity(params);

				// Cria a requisição POST do Usuario
				HttpPost requestPostUser = new HttpPost("http://localhost:8080/usuario");
				StringEntity paramsUser = new StringEntity(jsonUsuario);
				requestPostUser.addHeader("content-type", "application/json");
				requestPostUser.setEntity(paramsUser);

				// Envia a requisição
				httpClient.execute(requestPost);
				httpClient.execute(requestPostUser);
			} else {
				// Cria o alerta informando que o estoque é insuficiente
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Estoque insuficiente");
				alert.setHeaderText("Não há estoque suficiente para o pedido.");
				alert.setContentText("Por favor, verifique a quantidade de ingredientes no estoque.");
				alert.showAndWait();
			}

		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipoBiscoito.getItems().add("Biscoito Simples");
		tipoBiscoito.getItems().add("Biscoito Recheado");
		ThreadClientePedido threadPedido = new ThreadClientePedido(this);
	}

}
