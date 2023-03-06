package model;

public class Pedido {
	private String tipoPedido;
	private Ingredientes ingrediente;
	private int ID;
	
	public Pedido(String tipoPedido, double A, double B, double C, int ID) {
		this.tipoPedido = tipoPedido;
		this.ID = ID;
		ingrediente = new Ingredientes(A,B,C);
	}
	
	public String getTipoPedido() {
		return tipoPedido;
	}
	
	public int getID() {
		return ID;
	}

	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public double getIngredienteA() {
		return ingrediente.getQntIngrediente1();
	}
	
	public double getIngredienteB() {
		return ingrediente.getQntIngrediente2();
	}	
	
	public double getIngredienteC() {
		return ingrediente.getQntIngrediente3();
	}	
	
	public double somaIngredientes() {
		return ingrediente.getQntIngrediente1()+ingrediente.getQntIngrediente2()+ingrediente.getQntIngrediente3();
	}
	
	
}
