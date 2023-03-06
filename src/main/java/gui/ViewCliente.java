package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Estoque;
import model.Pedido;
import model.Usuario;
import service.ThreadCliente;

public class ViewCliente implements Initializable {

	private Gson gson = new Gson();
	private int i = 1;

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
	private TextField txtIngrediente1Estoque;
	@FXML
	private TextField txtIngrediente2Estoque;
	@FXML
	private TextField txtIngrediente3Estoque;
	@FXML
	private Button btConectar;
	@FXML
	private Button btEstoque;
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private TableView<Pedido> tabelaPedidos;
	@FXML
	private TableColumn<Pedido, String> colunaTipoBiscoito;
	@FXML
	private TableColumn<Pedido, Double> colunaIngrediente1;
	@FXML
	private TableColumn<Pedido, Double> colunaIngrediente2;
	@FXML
	private TableColumn<Pedido, Double> colunaIngrediente3;
	@FXML
	private TableColumn<Pedido, Double> somaIngredientes;
	@FXML
	private TableColumn<Pedido, Integer> colunaIDPedido;
	@FXML
	private TableColumn<Pedido, String> colunaNome;
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

	public void onBtEstoque() {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// Cria o objeto Estoque
			Estoque estoque = new Estoque(Double.parseDouble(txtIngrediente1Estoque.getText()),
					Double.parseDouble(txtIngrediente2Estoque.getText()),
					Double.parseDouble(txtIngrediente3Estoque.getText()));

			// Envia uma solicitação POST para atualizar o estoque
			HttpPost request = new HttpPost("http://localhost:8080/estoque");
			StringEntity params = new StringEntity(new Gson().toJson(estoque));
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			httpClient.execute(request);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	// Declara uma lista de IDs de pedidos já adicionados ao gráfico
	private XYChart.Series<Number, Number> dataPedidos;

	private List<Integer> pedidosAdicionados = new ArrayList<>();
	private int contadorPedidos = 0;

	public void lineChartPedidos() {
		// Define o eixo X como número
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Número do Pedido");

		// Define o eixo Y como número
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Quantidade de Ingredientes");

		lineChart.setTitle("Gráfico de Quantidade de Ingredientes por Tipo de Biscoito");
		lineChart.setAxisSortingPolicy(SortingPolicy.X_AXIS);
		lineChart.setCreateSymbols(false); // não cria símbolos para os pontos

		if (dataPedidos == null) {
			// Cria a série de dados para os pedidos, se ainda não foi criada
			dataPedidos = new XYChart.Series<>();
			dataPedidos.setName("Pedidos");
			lineChart.getData().add(dataPedidos);
			dataPedidos.getNode().setStyle("-fx-stroke: #336699;");
		}

		try {
			URL url = new URL("http://localhost:8080/pedidopronto");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			if (connection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String json = reader.readLine();

				// Convert the received JSON string into a list of Pedido objects
				LinkedList<Pedido> pedidosProntos = gson.fromJson(json, new TypeToken<LinkedList<Pedido>>() {
				}.getType());
				// Adiciona os dados dos pedidos que ainda não foram adicionados
				for (Pedido pedido : pedidosProntos) {
					if (!pedidosAdicionados.contains(pedido.getID())) {
						// Incrementa o contador de pedidos e cria o ponto com o número do pedido
						pedidosAdicionados.add(pedido.getID());
						contadorPedidos++;
						XYChart.Data<Number, Number> point = new XYChart.Data<>(contadorPedidos,
								pedido.somaIngredientes());
						Label label = new Label(Integer.toString(pedido.getID()));
						label.setPadding(new Insets(2, 5, 2, 5));
						point.setNode(label);
						dataPedidos.getData().add(point);
						label.setStyle(
								"-fx-background-color: white; -fx-border-color: #336699; -fx-text-fill: #336699;");
						Tooltip tooltip = new Tooltip(pedido.getTipoPedido());
						point.getNode().setOnMouseEntered(event -> Tooltip.install(point.getNode(), tooltip));
					}
				}
			} else {
				System.out.println("Erro na requisição: " + connection.getResponseCode());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void preencherTabela() {
		try {
			URL url = new URL("http://localhost:8080/pedidoprontotabela");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			URL url2 = new URL("http://localhost:8080/usuario");
			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
			connection2.setRequestMethod("GET");

			if (connection.getResponseCode() == 200 && connection2.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String json = reader.readLine();

				BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
				String json2 = reader2.readLine();

				// Converte o JSON string em uma lista de Pedido
				LinkedList<Pedido> pedidosProntos = gson.fromJson(json, new TypeToken<LinkedList<Pedido>>() {
				}.getType());

				LinkedList<Usuario> usuarios = gson.fromJson(json2, new TypeToken<LinkedList<Usuario>>() {
				}.getType());

				tabelaPedidos.getItems().setAll(pedidosProntos);
				colunaIDPedido.setCellValueFactory(new PropertyValueFactory<>("ID"));
				colunaTipoBiscoito.setCellValueFactory(new PropertyValueFactory<>("tipoPedido"));
				colunaIngrediente1.setCellValueFactory(new PropertyValueFactory<Pedido, Double>("ingredienteA"));
				colunaIngrediente2.setCellValueFactory(new PropertyValueFactory<Pedido, Double>("ingredienteB"));
				colunaIngrediente3.setCellValueFactory(new PropertyValueFactory<Pedido, Double>("ingredienteC"));
				somaIngredientes.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Pedido, Double>, ObservableValue<Double>>() {
							@Override
							public ObservableValue<Double> call(TableColumn.CellDataFeatures<Pedido, Double> dados) {
								return new SimpleDoubleProperty(dados.getValue().somaIngredientes()).asObject();
							}
						});
				colunaNome.setCellValueFactory(cellData -> {
					Pedido pedido = cellData.getValue();
					Usuario usuario = Usuario.procurarUsuarioPorID(usuarios, pedido.getID());
					return new SimpleStringProperty(usuario.getNome());
				});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipoBiscoito.getItems().add("Biscoito Simples");
		tipoBiscoito.getItems().add("Biscoito Recheado");
		ThreadCliente ThreadCliente = new ThreadCliente(this);
	}

}
