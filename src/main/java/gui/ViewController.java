package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import gui.model.Restricao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Pedido;
import model.Usuario;
import service.EsteiraTransportadoraA;
import service.EsteiraTransportadoraB;
import service.EsteiraTransportadoraC;
import service.GerenciadorPedidos;
import service.RelatorioProducao;

public class ViewController implements Initializable {
	// Instanciando classes para uso em métodos de botões da interface
	GerenciadorPedidos gerenciadorPedido = new GerenciadorPedidos();
	RelatorioProducao relatorio = new RelatorioProducao();

	// Variaveis usadas em itens no scene builder,
	// quando possui @FXML serve para uso de interface grafica do javaFX
	@FXML
	private TextField txtIngrediente1;
	@FXML
	private TextField txtIngrediente2;
	@FXML
	private TextField txtIngrediente3;
	@FXML
	private Label pedidosProntosFornoA;
	@FXML
	private Button btPedido;
	@FXML
	private ChoiceBox<String> tipoBiscoito;
	@FXML
	private ImageView imgBiscoitoA;
	@FXML
	private ImageView imgBiscoitoB;
	@FXML
	private ImageView imgBiscoitoC;
	@FXML
	private ImageView imgLedVerde;
	@FXML
	private ImageView imgLedVerdeB;
	@FXML
	private ImageView imgLedVerdeC;
	@FXML
	private ImageView imgLedLaranja;
	@FXML
	private ImageView imgLedLaranjaB;
	@FXML
	private ImageView imgLedLaranjaC;
	@FXML
	private ImageView imgLedVermelho;
	@FXML
	private ImageView imgLedVermelhoC;
	@FXML
	private ImageView botaoRelatorio;
	@FXML
	private Button btImgOn;
	@FXML
	private Button btImgOff;

	@FXML
	private TextField nomeUsuario;
	@FXML
	private Button btConectar;

	@FXML
	public void onBtPedido() { // Método para fazer o pedido, uso em botão da interface gráfica
		try {
			
			Random rand = new Random();
		    int randomNum = rand.nextInt(1000);
			
			Pedido pedido = new Pedido(tipoBiscoito.getSelectionModel().getSelectedItem(),
					Double.parseDouble(txtIngrediente1.getText()), Double.parseDouble(txtIngrediente2.getText()),
					Double.parseDouble(txtIngrediente3.getText()),randomNum);
			gerenciadorPedido.addNaFila(pedido);

			Restricao.showInformation("INFORMAÇÃO", "SEU PEDIDO FOI REALIZADO", null, AlertType.INFORMATION); // alerta
																												// gráfico
																												// na
																												// tela
																												// do
																												// usuario
		} catch (Exception e) {
			Restricao.showAlert("ALERTA", "FALTAM DADOS PARA O PEDIDO",
					"Escolha o tipo de pedido e insira os valores para os ingredientes.", AlertType.WARNING);
		}
	}

	// Método de uso no botão de imprimir o relatório
	public void onBtAddFila() {
		System.out.println(gerenciadorPedido.getSizeFilaA());
		pedidosProntosFornoA.setAlignment(Pos.TOP_CENTER);
		pedidosProntosFornoA.setText(RelatorioProducao.verRelatorio());

	}

	// Método obrigatório no JavaFX para iniciar variaveis em estados especificos
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// adiciono as opções de biscoito no ChoiceBox
		tipoBiscoito.getItems().add("Biscoito Simples");
		tipoBiscoito.getItems().add("Biscoito Recheado");
		// chamo o método da classe Restrição para envitar que o usuario use letras nos
		// campos de Ingrediente
		Restricao.setTextFieldDouble(txtIngrediente1);
		Restricao.setTextFieldDouble(txtIngrediente2);
		Restricao.setTextFieldDouble(txtIngrediente3);
		// inicializo as imagens de status de pedido (verde e vermelha) ocultas no
		// programa e o biscoito oculto também
		imgLedVerde.setVisible(false);
		imgLedVerdeB.setVisible(false);
		imgLedVerdeC.setVisible(false);
		imgLedVermelho.setVisible(false);
		imgLedVermelhoC.setVisible(false);
		imgBiscoitoA.setVisible(false);
		imgBiscoitoB.setVisible(false);
		imgBiscoitoC.setVisible(false);
		// Instancio a classe Esteira atribuindo os respectivos parametros
		EsteiraTransportadoraA esteiraA = new EsteiraTransportadoraA(imgBiscoitoA, imgLedVerde, imgLedLaranja,
				imgLedVermelho);
		EsteiraTransportadoraB esteiraB = new EsteiraTransportadoraB(imgBiscoitoB, imgLedVerdeB, imgLedLaranjaB);
		EsteiraTransportadoraC esteiraC = new EsteiraTransportadoraC(imgBiscoitoC, imgLedVerdeC, imgLedLaranjaC,
				imgLedVermelhoC);
		Thread threadGerenciador = new Thread(gerenciadorPedido, "Gerenciador");
		threadGerenciador.start();

	}
}
