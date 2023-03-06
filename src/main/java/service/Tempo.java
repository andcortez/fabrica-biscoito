package service;

public class Tempo {
	// atributos da classe tempo
	private static double tempoMaquinaIngrediente = 2;
	private static double tempoInicial = 5;
	
	// construtor vazio pois é necessário somente para acessar os métodos e atributos da classe
	public Tempo() {

	}

	public static double getTempoMaquina() {
		return tempoMaquinaIngrediente;
	}

	public static double getTempoInicial() {
		return tempoInicial;
	}
}
