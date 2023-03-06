package com.servidor;

import java.util.LinkedList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import model.Estoque;
import model.Pedido;
import model.Usuario;

@RestController
public class DataController {
	private Gson gson = new Gson();
	private LinkedList<Pedido> pedidoList = new LinkedList<>();
	private LinkedList<Usuario> usuarios = new LinkedList<>();
	private LinkedList<Pedido> pedidosProntos = new LinkedList<>();
	private LinkedList<Pedido> pedidosProntosTabela = new LinkedList<>();

	@GetMapping("/pedidos")
	public String getData() {
		try {
			return gson.toJson(pedidoList);
		} catch (Exception e) {
			return e.toString();
		}
	}

	@PostMapping("/pedidos")
	public void postData(@RequestBody String json) {
		try {
			Pedido pedido = gson.fromJson(json, Pedido.class);
			pedidoList.add(pedido);
		} catch (JsonSyntaxException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	@DeleteMapping("/pedidos")
	public ResponseEntity<String> deleteData() {
		if (pedidoList.isEmpty()) {
			return new ResponseEntity<>("Não há dados para remover", HttpStatus.NOT_FOUND);
		} else {
			pedidoList.clear();
			return new ResponseEntity<>("Dados removidos com sucesso", HttpStatus.OK);
		}

	}

	@GetMapping("/usuario")
	public String getDataUser() {
		try {
			return gson.toJson(usuarios);
		} catch (Exception e) {
			return e.toString();
		}
	}

	@PostMapping("/usuario")
	public void postDataUser(@RequestBody String json) {
		try {
			Usuario usuario = gson.fromJson(json, Usuario.class);
			usuarios.add(usuario);
			System.out.println("Pedido recebido no servidor");
		} catch (JsonSyntaxException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	@PostMapping("/pedidopronto")
	public void postDataPedidoPronto(@RequestBody String json) {
		try {
			Pedido pedido = gson.fromJson(json, Pedido.class);
			pedidosProntos.add(pedido);
		} catch (JsonSyntaxException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	@GetMapping("/pedidopronto")
	public String getDataPedidoPronto() {
		try {
			return gson.toJson(pedidosProntos);
		} catch (Exception e) {
			return e.toString();
		}
	}

	@DeleteMapping("/pedidopronto")
	public ResponseEntity<String> deleteDataPedidoPronto() {
		if (pedidosProntos.isEmpty()) {
			return new ResponseEntity<>("Não há pedidos prontos a serem removidos", HttpStatus.NOT_FOUND);
		} else {
			pedidosProntos.clear();
			return new ResponseEntity<>("Pedidos prontos removidos", HttpStatus.OK);
		}

	}

	@PostMapping("/pedidoprontotabela")
	public void postDataPedidoProntoTabela(@RequestBody String json) {
		try {
			Pedido pedido = gson.fromJson(json, Pedido.class);
			pedidosProntosTabela.add(pedido);
		} catch (JsonSyntaxException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	@GetMapping("/pedidoprontotabela")
	public String getDataPedidoProntoTabela() {
		try {
			return gson.toJson(pedidosProntosTabela);
		} catch (Exception e) {
			return e.toString();
		}
	}

}