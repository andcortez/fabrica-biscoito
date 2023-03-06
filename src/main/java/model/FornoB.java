package model;

import java.util.LinkedList;

import service.EsteiraTransportadoraC;
import service.RelatorioProducao;

public class FornoB implements Runnable {
	// criação dos atributos
	static LinkedList<Pedido> fornoB;
	Pedido pedido;
	private static double tempoForno = 0;
	private static boolean statusForno = false;
	private static double tempoInicial;
	private static int temPedido = 0;
	
	// construtor da classe, onde recebe o os atributos tempo e pedido
	public FornoB(double tempo, Pedido pedido) {
		FornoB.tempoInicial = tempo;
		this.pedido = pedido;
		FornoB.fornoB = new LinkedList<>();
		Thread fornoB = new Thread(this);
		fornoB.start();
	}

	public void addPedidoNoForno(Pedido pedido) {
		fornoB.add(pedido);
		temPedido = 1;
	}

	public static int possuiPedidoForno() {
		return temPedido;
	}

	public static void limpaForno() {
		fornoB.clear();// elimina o pedido dentro do forno
		statusForno = false;
		temPedido = 0;
		tempoForno = 0;
	}

	public static boolean getStatusForno() {
		return statusForno;
	}

	public static void setStatusFornoCheio() {
		statusForno = true;
	}

	public static void setStatusFornoVazio() {
		statusForno = false;
	}
	
	//método que faz o calculo do tempo que o pedido vai ficar no forno
	public static double calcularTempoForno(Pedido pedido) {
		if (pedido.getTipoPedido().equals("Biscoito Recheado")) {
			tempoForno = (tempoInicial + pedido.getIngredienteA() + pedido.getIngredienteB() + pedido.getIngredienteC())
					* 1.2;
		} else {
			tempoForno = tempoInicial + pedido.getIngredienteA() + pedido.getIngredienteB() + pedido.getIngredienteC();
		}
		return tempoForno;

	}

	public static double getTempoForno() {
		return tempoForno;
	}

	//thread que faz o forno ficar funcionando enquando os outros processos continuam rodando no sistema
	@Override
	public void run() {
		try {
			setStatusFornoCheio();
			addPedidoNoForno(pedido);
			calcularTempoForno(pedido);
			//Thread.sleep(500);
			Thread.sleep((long) (getTempoForno()) * 1000);
			RelatorioProducao.addPedidoNoRelatorio(pedido);
			RelatorioProducao.postPedidoPronto(pedido);
			limpaForno();
			EsteiraTransportadoraC.setImgLedVermelho();
			System.out.println("Pedido ficou pronto no Forno B");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
