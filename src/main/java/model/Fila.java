package model;

import java.util.LinkedList;

public class Fila{
	// cria uma coleção de pedidos usando linkedlist
	//essa fila pode ser tanto pedido biscoito simples como recheado
	LinkedList<Pedido> fila;

	public Fila() {
		this.fila = new LinkedList<>();
	}
	
	public int getTamanhoFila() {
		return fila.size();
	}

	public void addPedidoNaFila(Pedido pedido) {
		fila.add(pedido);
	}
	
	public void removePrimeiroFila() {
		fila.removeFirst();
	}
	
	public Pedido getFirstPedido() {
		return fila.pollFirst();
		
	}
}
