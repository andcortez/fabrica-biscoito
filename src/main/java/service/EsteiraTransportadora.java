package service;

import javafx.scene.image.ImageView;

public class EsteiraTransportadora implements Runnable { //implementa a interface runnable (thread)
	
	// atributos a serem usados nas esteiras
	protected GerenciadorPedidos pedidosGerenciados;
	protected ImageView imagemBiscoitoA;
	protected ImageView imagemBiscoitoB;
	protected ImageView imagemBiscoitoC;
	protected ImageView imagemLedVerde;
	protected ImageView imagemLedVerdeB;
	protected ImageView imagemLedVerdeC;
	protected ImageView imagemLedLaranja;
	protected ImageView imagemLedLaranjaB;
	protected ImageView imagemLedLaranjaC;
	protected static ImageView imagemLedVermelhoA;
	protected static ImageView imagemLedVermelhoC;

	public EsteiraTransportadora() {
		pedidosGerenciados = new GerenciadorPedidos();
		Thread esteira = new Thread(this, "Esteira Transportadora (Thread)");
		esteira.start();
	}

	@Override
	public void run() {
		// Sobrescrever esse método nas outras esteiras
	}

}
