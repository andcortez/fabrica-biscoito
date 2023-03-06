package service;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import model.FornoA;

public class EsteiraTransportadoraA extends EsteiraTransportadora { // extende de esteira transportadora (herança)
	
	// atributo especifico da classe
	private static boolean aguardando = false;
	
	// cria o construtor e recebe os atributos referente as imagens da interface
	public EsteiraTransportadoraA(ImageView imageBiscoitoA, ImageView imageLedVerde, ImageView imageLedLaranja,
			ImageView imageLedVermelho) {
		super(); // necessário pois é uma herança
		this.imagemBiscoitoA = imageBiscoitoA;
		this.imagemLedVerde = imageLedVerde;
		this.imagemLedLaranja = imageLedLaranja;
		EsteiraTransportadora.imagemLedVermelhoA = imageLedVermelho;
	}
	
	// método de esconder o led vermelho
	public static void setImgLedVermelho() {
		imagemLedVermelhoA.setVisible(false);
	}
	
	// métodos para verificar se há pedido pendente na esteira
	public static boolean isAguardando() {
		return aguardando;
	}

	public static void setAguardando(boolean aguardando) {
		EsteiraTransportadoraA.aguardando = aguardando;
	}
	
	// thread que executa a esteira A
	@Override
	public void run() {
		while (true) {
			imagemLedLaranja.setVisible(true);
			imagemLedVerde.setVisible(false);
			//System.out.println("rodando");
			//System.out.println(pedidosGerenciados.getSizeFilaA());
			try {
				Thread.sleep((long) (Math.random() * 2000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pedidosGerenciados.possuiPedidoFilaA() == true) {
				for (int i = 0; i < pedidosGerenciados.getSizeFilaA(); i++) {
					try {
						imagemBiscoitoA.setVisible(true);
						imagemLedLaranja.setVisible(false);
						imagemLedVerde.setVisible(true);
						pedidosGerenciados.inicioLinhaA();
						Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX()));
						Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX()));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaAIngrediente2();
						Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaAIngrediente3();
						Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));
						if (FornoA.getStatusForno() == false && FornoA.possuiPedidoForno() == 0
								&& EsteiraTransportadoraB.isAguardando() == false) {
							System.out.println("(ESTEIRA A) Forno A vazio");
							Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX() - 440));
							Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX() - 440));
							FornoA forno1 = new FornoA(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaA());
							imagemLedVermelhoA.setVisible(true);
							imagemBiscoitoA.setVisible(false);
						} else if (EsteiraTransportadoraB.isAguardando() == true) {
							System.out.println("(ESTEIRA A) Forno A cheio e esteira B aguardando!!");
							Platform.runLater(() -> imagemLedLaranja.setLayoutX(imagemLedLaranja.getLayoutX() + 620));
							imagemLedLaranja.setVisible(true);
							setAguardando(true);
							while (EsteiraTransportadoraB.isAguardando() == true) {
								Thread.sleep(1000);
							}
							Thread.sleep((long) ((FornoA.getTempoForno()) * 1000));
							System.out.println("(ESTEIRA A) Forno A ficou livre!!");
							Platform.runLater(() -> imagemLedLaranja.setLayoutX(imagemLedLaranja.getLayoutX() - 620));
							Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX() - 440));
							imagemLedLaranja.setVisible(false);
							Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX() - 440));
							FornoA forno1 = new FornoA(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaA());
							FornoA.setStatusFornoCheio();
							imagemBiscoitoA.setVisible(false);
							imagemLedVermelhoA.setVisible(true);
							setAguardando(false);
						} else {
							System.out.println("(ESTEIRA A) Forno A cheio!!");
							imagemLedLaranja.setVisible(true);
							Platform.runLater(() -> imagemLedLaranja.setLayoutX(imagemLedLaranja.getLayoutX() + 620));
							setAguardando(true);
							Thread.sleep((long) ((FornoA.getTempoForno()) * 1000));
							while (FornoA.possuiPedidoForno() == 1) {
								Thread.sleep(1000);
							}

							System.out.println("(ESTEIRA A) Forno A ficou livre!!");
							Platform.runLater(() -> imagemLedLaranja.setLayoutX(imagemLedLaranja.getLayoutX() - 620));
							Platform.runLater(() -> imagemLedVerde.setLayoutX(imagemLedVerde.getLayoutX() - 440));
							imagemLedLaranja.setVisible(false);
							Platform.runLater(() -> imagemBiscoitoA.setLayoutX(imagemBiscoitoA.getLayoutX() - 440));
							FornoA forno1 = new FornoA(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaA());
							imagemBiscoitoA.setVisible(false);
							imagemLedVermelhoA.setVisible(true);
							setAguardando(false);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
			imagemBiscoitoA.setVisible(false);
			imagemLedLaranja.setVisible(true);
		}
	}
}
