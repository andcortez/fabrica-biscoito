package service;

import java.util.LinkedList;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import model.Pedido;

public class RelatorioProducao {

	// cria uma coleção de objetos do tipo pedido
	private static LinkedList<Pedido> relatorio;

	public RelatorioProducao() {
		RelatorioProducao.relatorio = new LinkedList<>();
	}

	public static void addPedidoNoRelatorio(Pedido pedido) {
		relatorio.add(pedido);
	}

	// método para imprimir os pedidos que ficaram prontos
	public static String verRelatorio() {
		String pedidosProntos = "";
		for (int i = 0; i < relatorio.size(); i++) {
			Pedido pedido = relatorio.get(i);
			if (pedido.getTipoPedido().equals("Biscoito Recheado")) {
				pedidosProntos += "Pedido de número: " + (i + 1) + "- " + pedido.getTipoPedido()
						+ "-  Tempo total gasto:"
						+ (1.2 * (Tempo.getTempoInicial() + pedido.getIngredienteA() + pedido.getIngredienteB()
								+ pedido.getIngredienteC()))
						+ " segundos" + "- Ingrediente total gasto:"
						+ (pedido.getIngredienteA() + pedido.getIngredienteB() + pedido.getIngredienteC()) + " Kg\n"
						+ "\n";
			} else {
				pedidosProntos += "Pedido de número:" + (i + 1) + "- " + pedido.getTipoPedido()
						+ "-  Tempo total gasto:"
						+ (Tempo.getTempoInicial() + pedido.getIngredienteA() + pedido.getIngredienteB()
								+ pedido.getIngredienteC())
						+ " segundos" + "- Ingrediente total gasto:"
						+ (pedido.getIngredienteA() + pedido.getIngredienteB() + pedido.getIngredienteC()) + " Kg\n"
						+ "\n";
			}
		}
		return pedidosProntos;
	}
	// Método que abre conexão com o servidor e envia um POST com o pedido pronto do forno
	public static void postPedidoPronto(Pedido pedido) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			// Converte o objeto Pedido para JSON
			Gson gson = new Gson();
			String jsonPedidoPronto = gson.toJson(pedido);
			// Cria a requisição POST do Pedido
			HttpPost requestPost = new HttpPost("http://localhost:8080/pedidopronto");
			StringEntity param = new StringEntity(jsonPedidoPronto);
			requestPost.addHeader("content-type", "application/json");
			requestPost.setEntity(param);
			httpClient.execute(requestPost);
			
			CloseableHttpClient httpClient2 = HttpClientBuilder.create().build();
			// Converte o objeto Pedido para JSON
			Gson gson2 = new Gson();
			String jsonPedidoProntoTabela = gson2.toJson(pedido);
			// Cria a requisição POST do Pedido
			HttpPost requestPost2 = new HttpPost("http://localhost:8080/pedidoprontotabela");
			StringEntity param2 = new StringEntity(jsonPedidoProntoTabela);
			requestPost2.addHeader("content-type", "application/json");
			requestPost2.setEntity(param2);
			httpClient2.execute(requestPost2);
			
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

}
