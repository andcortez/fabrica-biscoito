package model;

public class Estoque {
	private double ingrediente1;
	private double ingrediente2;
	private double ingrediente3;
	
	public Estoque(double ingrediente1, double ingrediente2, double ingrediente3) {
		this.ingrediente1 = ingrediente1;
		this.ingrediente2 = ingrediente2;
		this.ingrediente3 = ingrediente3;
	}

	public Estoque() {
	}

	public double getIngrediente1() {
		return ingrediente1;
	}

	public double getIngrediente2() {
		return ingrediente2;
	}

	public double getIngrediente3() {
		return ingrediente3;
	}

	public void descontarIngrediente1(double quantidade) {
		ingrediente1 -= quantidade;
	}

	public void descontarIngrediente2(double quantidade) {
		ingrediente2 -= quantidade;
	}

	public void descontarIngrediente3(double quantidade) {
		ingrediente3 -= quantidade;
	}

	public boolean verificarEstoqueIngrediente1(double quantidade) {
		if (ingrediente1 >= quantidade) {
			return true;
		}
		return false;
	}

	public boolean verificarEstoqueIngrediente2(double quantidade) {
		if (ingrediente2 >= quantidade) {
			return true;
		}
		return false;
	}

	public boolean verificarEstoqueIngrediente3(double quantidade) {
		if (ingrediente3 >= quantidade) {
			return true;
		}
		return false;
	}

	public void adicionarIngrediente1(double quantidade) {
		ingrediente1 += quantidade;
	}

	public void adicionarIngrediente2(double quantidade) {
		ingrediente2 += quantidade;
	}

	public void adicionarIngrediente3(double quantidade) {
		ingrediente3 += quantidade;
	}
	
	public boolean verificarEstoqueSuficiente(double quantidadeIngrediente1, double quantidadeIngrediente2, double quantidadeIngrediente3) {
	    if (verificarEstoqueIngrediente1(quantidadeIngrediente1) && verificarEstoqueIngrediente2(quantidadeIngrediente2) && verificarEstoqueIngrediente3(quantidadeIngrediente3)) {
	        descontarIngrediente1(quantidadeIngrediente1);
	        descontarIngrediente2(quantidadeIngrediente2);
	        descontarIngrediente3(quantidadeIngrediente3);
	        return true;
	    } else {
	        return false;
	    }
	}

}