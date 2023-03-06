package service;

import gui.ViewClienteDados;
import javafx.application.Platform;

public class ThreadClienteDados implements Runnable {

	private ViewClienteDados viewClienteDados;

	public ThreadClienteDados(ViewClienteDados viewCliente) {
		this.viewClienteDados = viewCliente;
		Thread grafico = new Thread(this, "grafico");
		grafico.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
				Platform.runLater(() -> {
					viewClienteDados.lineChartPedidos();
				});
				Platform.runLater(() -> {
					viewClienteDados.preencherTabela();
				});
				Platform.runLater(() -> {
					viewClienteDados.atualizarLabelsEstoque();
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
