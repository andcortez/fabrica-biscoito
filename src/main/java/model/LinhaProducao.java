package model;

import java.util.LinkedList;

public class LinhaProducao {
	// cria uma coleção de pedidos usando linkedlist
	//essa linha pode ser tanto pedido biscoito simples como recheado
	LinkedList<Pedido> linha; // linha=esteira

	public LinhaProducao() {
		this.linha = new LinkedList<>();
	}

	public int getTamanhoLinha() {
		return linha.size(); // retorna o tamanho da linkedlist
	}

	public void addMovimentacaoLinha(Pedido pedido) {
		linha.add(pedido); // adiciona o pedido na esteira
	}

	public Pedido removeEtapaIngrediente() {
		return linha.pollFirst(); // remove o pedido de uma etapa do ingrediente
	}
	
	public Pedido getFirstPedido() {
		return linha.pollFirst(); // recebe o primeiro pedido da linha e ja o remove
		
	}

}
