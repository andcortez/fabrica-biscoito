package model;

public class Ingredientes {
	// cria os atributos para a classe ingrediente
	private double qntIngrediente1;
	private double qntIngrediente2;
	private double qntIngrediente3;
	// construtor da classe que recebe os atributos por parametro
	public Ingredientes(double qntIngrediente1, double qntIngrediente2, double qntIngrediente3) {
		this.qntIngrediente1 = qntIngrediente1;
		this.qntIngrediente2 = qntIngrediente2;
		this.qntIngrediente3 = qntIngrediente3;
	}

	public double getQntIngrediente1() {
		return qntIngrediente1;
	}

	public void setQntIngrediente1(int qntIngrediente1) {
		this.qntIngrediente1 = qntIngrediente1;
	}

	public double getQntIngrediente2() {
		return qntIngrediente2;
	}

	public void setQntIngrediente2(int qntIngrediente2) {
		this.qntIngrediente2 = qntIngrediente2;
	}

	public double getQntIngrediente3() {
		return qntIngrediente3;
	}

	public void setQntIngrediente3(int qntIngrediente3) {
		this.qntIngrediente3 = qntIngrediente3;
	}
	
	
}
