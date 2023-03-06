package service;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import model.FornoB;

public class EsteiraTransportadoraC extends EsteiraTransportadora {

	// atributo especifico da classe
	private static boolean aguardando = false;

	// cria o construtor e recebe os atrbutos referente as imagens da interface
	public EsteiraTransportadoraC(ImageView imageBiscoitoC, ImageView imageLedVerde, ImageView imageLedLaranja,
			ImageView imageLedVermelho) {
		super();// necessário pois é uma herança
		this.imagemBiscoitoC = imageBiscoitoC;
		this.imagemLedVerdeC = imageLedVerde;
		this.imagemLedLaranjaC = imageLedLaranja;
		EsteiraTransportadora.imagemLedVermelhoC = imageLedVermelho;
	}

	// método de esconder o led vermelho
	public static void setImgLedVermelho() {
		imagemLedVermelhoC.setVisible(false);
	}

	// métodos para verificar se há pedido pendente na esteira
	public static boolean isAguardando() {
		return aguardando;
	}

	public static void setAguardando(boolean aguardando) {
		EsteiraTransportadoraC.aguardando = aguardando;
	}

	// thread que executa a esteira C
	@Override
	public void run() {
		while (true) {
			imagemLedLaranjaC.setVisible(true);
			imagemLedVerdeC.setVisible(false);
			try {
				Thread.sleep((long) (Math.random() * 2000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pedidosGerenciados.possuiPedidoFilaC() == true) {
				for (int i = 0; i < pedidosGerenciados.getSizeFilaC(); i++) {
					try {
						imagemBiscoitoC.setVisible(true);
						imagemLedLaranjaC.setVisible(false);
						imagemLedVerdeC.setVisible(true);
						pedidosGerenciados.inicioLinhaC();
						Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX()));
						Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX()));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaCIngrediente2();
						Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));

						pedidosGerenciados.linhaCIngrediente3();
						Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX() + 220));
						Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX() + 220));
						Thread.sleep((long) (Tempo.getTempoMaquina() * 1000));
						if (FornoB.getStatusForno() == false && FornoB.possuiPedidoForno() == 0
								&& EsteiraTransportadoraB.isAguardando() == false) {
							System.out.println("(ESTEIRA C) Forno B vazio");
							Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX() - 440));
							Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX() - 440));
							FornoB forno2 = new FornoB(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaC());
							imagemLedVermelhoC.setVisible(true);
							imagemBiscoitoC.setVisible(false);
						} else if (EsteiraTransportadoraB.isAguardando() == true) {
							System.out.println("(ESTEIRA C) Forno B cheio e esteira B aguardando!!");
							Platform.runLater(() -> imagemLedLaranjaC.setLayoutX(imagemLedLaranjaC.getLayoutX() + 620));
							imagemLedLaranjaC.setVisible(true);
							setAguardando(true);
							while (EsteiraTransportadoraB.isAguardando() == true) {
								Thread.sleep(1000);
							}
							Thread.sleep((long) ((FornoB.getTempoForno()) * 1000));
							System.out.println("(ESTEIRA C) Forno B ficou livre!!");
							Platform.runLater(() -> imagemLedLaranjaC.setLayoutX(imagemLedLaranjaC.getLayoutX() - 620));
							Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX() - 440));
							imagemLedLaranjaC.setVisible(false);
							Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX() - 440));
							FornoB forno2 = new FornoB(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaC());
							imagemBiscoitoC.setVisible(false);
							imagemLedVermelhoC.setVisible(true);
							setAguardando(false);
						} else {
							System.out.println("(ESTEIRA C) Forno B cheio!!");
							imagemLedLaranjaC.setVisible(true);
							Platform.runLater(() -> imagemLedLaranjaC.setLayoutX(imagemLedLaranjaC.getLayoutX() + 620));
							setAguardando(true);
							Thread.sleep((long) ((FornoB.getTempoForno()) * 1000));
							while (FornoB.possuiPedidoForno() == 1) {
								Thread.sleep(1000);
							}
							System.out.println("(ESTEIRA C) Forno B ficou livre!!");
							Platform.runLater(() -> imagemLedLaranjaC.setLayoutX(imagemLedLaranjaC.getLayoutX() - 620));
							Platform.runLater(() -> imagemLedVerdeC.setLayoutX(imagemLedVerdeC.getLayoutX() - 440));
							imagemLedLaranjaC.setVisible(false);
							Platform.runLater(() -> imagemBiscoitoC.setLayoutX(imagemBiscoitoC.getLayoutX() - 440));
							FornoB forno2 = new FornoB(Tempo.getTempoInicial(), pedidosGerenciados.getPedidoLinhaC());
							imagemBiscoitoC.setVisible(false);
							imagemLedVermelhoC.setVisible(true);
							setAguardando(false);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			imagemBiscoitoC.setVisible(false);
			imagemLedLaranjaC.setVisible(true);
		}
	}
}
