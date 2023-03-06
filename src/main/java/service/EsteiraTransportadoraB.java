package service;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import model.FornoA;
import model.FornoB;

public class EsteiraTransportadoraB extends EsteiraTransportadora {

	// atributo especifico da classe
	private static boolean aguardando = false;

	// cria o construtor e recebe os atrbutos referente as imagens da interface
	public EsteiraTransportadoraB(ImageView imageBiscoito, ImageView imageLedVerde, ImageView imageLedLaranja) {
		super();// necessário pois é uma herança
		this.imagemBiscoitoB = imageBiscoito;
		this.imagemLedVerdeB = imageLedVerde;
		this.imagemLedLaranjaB = imageLedLaranja;
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
		EsteiraTransportadoraB.aguardando = aguardando;
	}
	
	// thread que executa a esteira B
	@Override
	public void run() {
		while (true) {
			imagemLedLaranjaB.setVisible(true);
			imagemLedVerdeB.setVisible(false);
			try {
				Thread.sleep((long) (Math.random() * 2000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pedidosGerenciados.possuiPedidoFilaB() == true) {
				for (int i = 0; i < pedidosGerenciados.getSizeFilaB(); i++) {
					try {
						imagemBiscoitoB.setVisible(true);
						imagemLedLaranjaB.setVisible(false);
						imagemLedVerdeB.setVisible(true);
						pedidosGerenciados.inicioLinhaB();
						Platform.runLater(() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX()));
						Platform.runLater(() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX()));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaBIngrediente2();
						Platform.runLater(() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaBIngrediente3();
						Platform.runLater(() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));
						if (FornoA.getStatusForno() == false && FornoA.possuiPedidoForno() == 0) {
							System.out.println("(ESTEIRA B) Forno A vazio");
							Platform.runLater(() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() - 440));
							Platform.runLater(() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() - 440));
							FornoA forno1 = new FornoA(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaB());
							imagemLedVermelhoA.setVisible(true);
							imagemBiscoitoB.setVisible(false);
						} else if (FornoB.getStatusForno() == false && FornoB.possuiPedidoForno() == 0) {
							System.out.println("(ESTEIRA B) Forno B vazio");
							Platform.runLater(() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() - 440));
							Platform.runLater(() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() - 440));
							FornoB forno2 = new FornoB(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaB());
							imagemLedVermelhoC.setVisible(true);
							imagemBiscoitoB.setVisible(false);
						} else {
							System.out.println("(ESTEIRA B) Fornos A e B cheios!!");
							Platform.runLater(() -> imagemLedLaranjaB.setLayoutX(imagemLedLaranjaB.getLayoutX() + 620));
							imagemLedLaranjaB.setVisible(true);
							while (true) {
								setAguardando(true);
								Thread.sleep(1000);
								if (EsteiraTransportadoraC.isAguardando() == false && FornoB.possuiPedidoForno() == 0
										&& FornoB.getStatusForno() == false) {
									Thread.sleep((long) ((FornoB.getTempoForno()) * 1000));
									System.out.println("(ESTEIRA B) Forno B ficou livre!!!");
									Platform.runLater(
											() -> imagemLedLaranjaB.setLayoutX(imagemLedLaranjaB.getLayoutX() - 620));
									Platform.runLater(
											() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() - 440));
									imagemLedLaranjaB.setVisible(false);
									Platform.runLater(
											() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() - 440));
									FornoB forno2 = new FornoB(Tempo.getTempoInicial(),
											pedidosGerenciados.getPedidoLinhaB());
									imagemBiscoitoB.setVisible(false);
									imagemLedVermelhoC.setVisible(true);
									setAguardando(false);
									break;
								} else if (EsteiraTransportadoraA.isAguardando() == false
										&& FornoA.possuiPedidoForno() == 0 && FornoA.getStatusForno() == false) {
									Thread.sleep((long) ((FornoA.getTempoForno()) * 1000));
									System.out.println("(ESTEIRA B) Forno A ficou livre!!!");
									Platform.runLater(
											() -> imagemLedLaranjaB.setLayoutX(imagemLedLaranjaB.getLayoutX() - 620));
									Platform.runLater(
											() -> imagemLedVerdeB.setLayoutX(imagemLedVerdeB.getLayoutX() - 440));
									imagemLedLaranjaB.setVisible(false);
									Platform.runLater(
											() -> imagemBiscoitoB.setLayoutX(imagemBiscoitoB.getLayoutX() - 440));
									FornoA forno1 = new FornoA(Tempo.getTempoInicial(),
											pedidosGerenciados.getPedidoLinhaB());
									imagemBiscoitoB.setVisible(false);
									imagemLedVermelhoA.setVisible(true);
									setAguardando(false);
									break;
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
			imagemBiscoitoB.setVisible(false);
			imagemLedLaranjaB.setVisible(true);
		}
	}
}
