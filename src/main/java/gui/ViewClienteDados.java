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
import java.util.HashSet;
import java.util.LinkedList;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Estoque;
import model.Pedido;
import model.Usuario;
import service.ThreadCliente;
import service.ThreadClienteDados;

public class ViewClienteDados implements Initializable {

	private Gson gson = new Gson();
	private int i = 1;
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

	// Declara uma lista de IDs de pedidos já adicionados ao gráfico
	private HashSet<Integer> pedidosAdicionados = new HashSet<Integer>();

	public void lineChartPedidos() {
		// Define o eixo X como String
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Número do Pedido");

		// Define o eixo Y como número
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Quantidade de Ingredientes");

		lineChart.setTitle("Gráfico de Quantidade de Ingredientes por Tipo de Biscoito");
		lineChart.setAxisSortingPolicy(SortingPolicy.X_AXIS);

		try {
			URL url = new URL("http://localhost:8080/pedidopronto");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			if (connection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String json = reader.readLine();

				LinkedList<Pedido> pedidosProntos = gson.fromJson(json, new TypeToken<LinkedList<Pedido>>() {
				}.getType());

				// Adiciona os dados dos pedidos que ainda não foram adicionados
				for (Pedido pedido : pedidosProntos) {
					if (!pedidosAdicionados.contains(pedido.getID())) {
						XYChart.Series<Number, Number> data = new XYChart.Series<>();
						data.setName(pedido.getTipoPedido() + " - " + "ID: " + pedido.getID());
						data.getData().add(new XYChart.Data<>(i, pedido.somaIngredientes()));
						lineChart.getData().add(data);
						data.getNode().setStyle("-fx-stroke: #336699; ");
						// Adiciona o ID do pedido à lista de pedidos já adicionados
						pedidosAdicionados.add(pedido.getID());
						i++;
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
		ThreadClienteDados ThreadClienteDados = new ThreadClienteDados(this);
	}

}
