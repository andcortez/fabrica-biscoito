package model;

import java.util.LinkedList;

public class Usuario {
	private String nome;
	private int ID;
	
	public Usuario(String nome, int ID) {
		this.nome = nome;
		this.ID = ID;
	}

	public String getNome() {
		return nome;
	}
	
	public int getID() {
		return ID;
	}
	
	public static Usuario procurarUsuarioPorID(LinkedList<Usuario> listaUsuarios, int id) {
	    for (Usuario usuario : listaUsuarios) {
	        if (usuario.getID() == id) {
	            return usuario;
	        }
	    }
	    return null;
	}
}
