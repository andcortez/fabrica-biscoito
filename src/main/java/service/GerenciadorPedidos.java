package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Estoque;
import model.Fila;
import model.LinhaProducao;
import model.Pedido;

public class GerenciadorPedidos implements Runnable {

	Pedido pedido;
	private Gson gson = new Gson();
	private final Object lock = new Object();

	// cria as filas A, B e C
	private static Fila filaA = new Fila();
	private static Fila filaB = new Fila();
	private static Fila filaC = new Fila();

	// Cria as estapas da linha de produção (esteiras)
	private static LinhaProducao linhaA = new LinhaProducao();
	private static LinhaProducao linhaAaux = new LinhaProducao();
	private static LinhaProducao linhaAaux2 = new LinhaProducao();

	private static LinhaProducao linhaB = new LinhaProducao();
	private static LinhaProducao linhaBaux = new LinhaProducao();
	private static LinhaProducao linhaBaux2 = new LinhaProducao();

	private static LinhaProducao linhaC = new LinhaProducao();
	private static LinhaProducao linhaCaux = new LinhaProducao();
	private static LinhaProducao linhaCaux2 = new LinhaProducao();

	// variaveis para monitorar o tamanho das filas
	private int sizeFilaA = 0;
	private int sizeFilaB = 0;
	private int sizeFilaC = 0;

	/*
	 * public GerenciadorPedidos(Pedido pedido) { this.pedido = pedido; Thread
	 * threadGerenciador = new Thread(this, "Gerenciador");
	 * threadGerenciador.start();
	 * 
	 * }
	 */

	public GerenciadorPedidos() {
		// Thread threadGerenciador = new Thread(this, "Gerenciador");
		// threadGerenciador.start();

	}

	public Pedido getPedidoLinhaA() { // pega o pedido na ultima etapa da esteira A
		return linhaAaux2.getFirstPedido();
	}

	public Pedido getPedidoLinhaB() { // pega o pedido na ultima etapa da esteira A
		return linhaBaux2.getFirstPedido();
	}

	public Pedido getPedidoLinhaC() { // pega o pedido na ultima etapa da esteira A
		return linhaCaux2.getFirstPedido();
	}

	public double getSizeFilaA() {
		return sizeFilaA;
	}

	public double getSizeFilaB() {
		return sizeFilaB;
	}

	public double getSizeFilaC() {
		return sizeFilaC;
	}

	// MÉTODOS PARA CRIAÇÃO DE FILA DE PEDIDOS
	public void addNaFila(Pedido pedido) {
		verificarTamanhoFilas();
		if (sizeFilaA <= sizeFilaB && sizeFilaA <= sizeFilaC) {
			setFilaA(pedido);
		} else if (sizeFilaB < sizeFilaA && sizeFilaB <= sizeFilaC) {
			setFilaB(pedido);
		} else {
			setFilaC(pedido);
		}
	}

	public void setFilaA(Pedido pedido) {
		filaA.addPedidoNaFila(pedido);
	}

	public void setFilaB(Pedido pedido) {
		filaB.addPedidoNaFila(pedido);
	}

	public void setFilaC(Pedido pedido) {
		if (verificaPedido(pedido) == true) {
			filaC.addPedidoNaFila(pedido);
		} else if (verificaPedido(pedido) == false && filaA.getTamanhoFila() <= filaB.getTamanhoFila()) {
			setFilaA(pedido);
		} else {
			setFilaB(pedido);
		}
	}

	public boolean verificaPedido(Pedido pedido) {
		if (pedido.getTipoPedido().equals("Biscoito Simples")) {
			return true;
		} else {
			return false;
		}
	}

	public void verificarTamanhoFilas() {
		sizeFilaA = filaA.getTamanhoFila();
		sizeFilaB = filaB.getTamanhoFila();
		sizeFilaC = filaC.getTamanhoFila();
	}

	public int verTamFilaA() {
		return filaA.getTamanhoFila();
	}

	public int verTamFilaB() {
		return filaB.getTamanhoFila();
	}

	public int verTamFilaC() {
		return filaC.getTamanhoFila();
	}

	// MÉTODOS PARA LINHA DE PRODUÇÃO (ESTEIRA)

	// Retira o primeiro pedido da fila A e coloca na esteira A
	public void inicioLinhaA() {
		if (filaA.getTamanhoFila() >= 1) {
			Pedido pedido = filaA.getFirstPedido();
			linhaA.addMovimentacaoLinha(pedido);
		}
	}

	public void linhaAIngrediente2() {
		if (linhaA.getTamanhoLinha() == 1) {
			Pedido pedido = linhaA.removeEtapaIngrediente();
			linhaAaux.addMovimentacaoLinha(pedido);
		}
	}

	public void linhaAIngrediente3() {
		if (linhaAaux.getTamanhoLinha() == 1) {
			Pedido pedido = linhaAaux.removeEtapaIngrediente();
			linhaAaux2.addMovimentacaoLinha(pedido);
		}
	}

	// Retira o primeiro pedido da fila B e coloca na esteira B
	public void inicioLinhaB() {
		if (filaB.getTamanhoFila() >= 1) {
			Pedido pedido = filaB.getFirstPedido();
			linhaB.addMovimentacaoLinha(pedido);
		}

	}

	public void linhaBIngrediente2() {
		if (linhaB.getTamanhoLinha() == 1) {
			Pedido pedido = linhaB.removeEtapaIngrediente();
			linhaBaux.addMovimentacaoLinha(pedido);
		}
	}

	public void linhaBIngrediente3() {
		if (linhaBaux.getTamanhoLinha() == 1) {
			Pedido pedido = linhaBaux.removeEtapaIngrediente();
			linhaBaux2.addMovimentacaoLinha(pedido);
		}
	}

	// Retira o primeiro pedido da fila C e coloca na esteira C
	public void inicioLinhaC() {
		if (filaC.getTamanhoFila() >= 1) {
			Pedido pedido = filaC.getFirstPedido();
			linhaC.addMovimentacaoLinha(pedido);
		}

	}

	public void linhaCIngrediente2() {
		if (linhaC.getTamanhoLinha() == 1) {
			Pedido pedido = linhaC.removeEtapaIngrediente();
			linhaCaux.addMovimentacaoLinha(pedido);
		}
	}

	public void linhaCIngrediente3() {
		if (linhaCaux.getTamanhoLinha() == 1) {
			Pedido pedido = linhaCaux.removeEtapaIngrediente();
			linhaCaux2.addMovimentacaoLinha(pedido);
		}
	}

	public int verTamLinhaA() {
		return linhaAaux2.getTamanhoLinha();
	}

	public int verTamLinhaB() {
		return linhaBaux2.getTamanhoLinha();
	}

	public int verTamLinhaC() {
		return linhaCaux2.getTamanhoLinha();
	}

	public boolean possuiPedidoFilaA() {
		verificarTamanhoFilas();
		if (sizeFilaA > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean possuiPedidoFilaB() {
		verificarTamanhoFilas();
		if (sizeFilaB > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean possuiPedidoFilaC() {
		verificarTamanhoFilas();
		if (sizeFilaC > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
				URL url = new URL("http://localhost:8080/pedidos");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				if (connection.getResponseCode() == 200) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String json = reader.readLine();
					
					LinkedList<Pedido> pedidos = gson.fromJson(json, new TypeToken<LinkedList<Pedido>>() {
					}.getType());

					HttpClient client = HttpClient.newHttpClient();
					HttpRequest request = HttpRequest.newBuilder()
							.uri(URI.create("http://localhost:8080/estoque")).GET().build();
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
					Estoque estoque = gson.fromJson(response.body(), Estoque.class);
					
					if (!pedidos.isEmpty()) {
						Pedido pedido = pedidos.getFirst();
						if (estoque.verificarEstoqueSuficiente(pedido.getIngredienteA(), pedido.getIngredienteB(),
								pedido.getIngredienteC())) {
							
							request = HttpRequest.newBuilder()
									.uri(URI.create("http://localhost:8080/descontar-ingrediente"
											+ "?ingrediente=ingrediente1" + "&quantidade=" + pedido.getIngredienteA()))
									.POST(HttpRequest.BodyPublishers.noBody()).build();

							response = client.send(request, HttpResponse.BodyHandlers.ofString());

							client = HttpClient.newHttpClient();
							request = HttpRequest.newBuilder()
									.uri(URI.create("http://localhost:8080/descontar-ingrediente"
											+ "?ingrediente=ingrediente2" + "&quantidade=" + pedido.getIngredienteB()))
									.POST(HttpRequest.BodyPublishers.noBody()).build();

							response = client.send(request, HttpResponse.BodyHandlers.ofString());

							client = HttpClient.newHttpClient();
							request = HttpRequest.newBuilder()
									.uri(URI.create("http://localhost:8080/descontar-ingrediente"
											+ "?ingrediente=ingrediente3" + "&quantidade=" + pedido.getIngredienteC()))
									.POST(HttpRequest.BodyPublishers.noBody()).build();

							response = client.send(request, HttpResponse.BodyHandlers.ofString());

							addNaFila(pedido);
							HttpClient client2 = HttpClient.newHttpClient();
							HttpRequest request2 = HttpRequest.newBuilder()
									.uri(URI.create("http://localhost:8080/pedidos")).DELETE().build();

							HttpResponse<String> response2 = client2.send(request2,
									HttpResponse.BodyHandlers.ofString());
							System.out.println(response2.body());
						} else {
							System.out.println("Estoque insuficiente para o pedido");
						}

					}
				} else {
					System.out.println("Erro na requisição: " + connection.getResponseCode());
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}

}
