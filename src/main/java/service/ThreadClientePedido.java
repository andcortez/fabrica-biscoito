package service;

import gui.ViewClientePedido;
import javafx.application.Platform;

public class ThreadClientePedido implements Runnable {

	private ViewClientePedido viewClientePedido;

	public ThreadClientePedido(ViewClientePedido viewCliente) {
		this.viewClientePedido = viewCliente;
		Thread grafico = new Thread(this, "grafico");
		grafico.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
				Platform.runLater(() -> {
					viewClientePedido.atualizarLabelsEstoque();
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
