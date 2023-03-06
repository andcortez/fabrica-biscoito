package service;

import gui.ViewCliente;
import javafx.application.Platform;

public class ThreadCliente implements Runnable {

	private ViewCliente viewCliente;

	public ThreadCliente(ViewCliente viewCliente) {
		this.viewCliente = viewCliente;
		Thread grafico = new Thread(this, "grafico");
		grafico.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
				Platform.runLater(() -> {
					viewCliente.lineChartPedidos();
				});
				Platform.runLater(() -> {
					viewCliente.preencherTabela();
				});
				Platform.runLater(() -> {
					viewCliente.atualizarLabelsEstoque();
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
