package com.servidor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Estoque;
import model.Pedido;

@RestController
public class EstoqueController {

  private Estoque estoque = new Estoque(3, 3, 3);

  @GetMapping("/estoque")
  public Estoque getEstoque() {
    return estoque;
  }
  
  @PostMapping("/estoque")
  public ResponseEntity<?> atualizarEstoque(@RequestBody Estoque estoqueAtualizado) {
    estoque.adicionarIngrediente1(estoqueAtualizado.getIngrediente1());
    estoque.adicionarIngrediente2(estoqueAtualizado.getIngrediente2());
    estoque.adicionarIngrediente3(estoqueAtualizado.getIngrediente3());
    return ResponseEntity.ok().build();
  }
  @PostMapping("/descontar-ingrediente")
  public ResponseEntity<?> descontarIngrediente(@RequestParam String ingrediente, @RequestParam double quantidade) {
      switch (ingrediente) {
          case "ingrediente1":
              if (estoque.verificarEstoqueIngrediente1(quantidade)) {
                  estoque.descontarIngrediente1(quantidade);
                  return ResponseEntity.ok("Ingrediente 1 descontado do estoque.");
              } else {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estoque insuficiente de Ingrediente 1.");
              }
          case "ingrediente2":
              if (estoque.verificarEstoqueIngrediente2(quantidade)) {
                  estoque.descontarIngrediente2(quantidade);
                  return ResponseEntity.ok("Ingrediente 2 descontado do estoque.");
              } else {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estoque insuficiente de Ingrediente 2.");
              }
          case "ingrediente3":
              if (estoque.verificarEstoqueIngrediente3(quantidade)) {
                  estoque.descontarIngrediente3(quantidade);
                  return ResponseEntity.ok("Ingrediente 3 descontado do estoque.");
              } else {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estoque insuficiente de Ingrediente 3.");
              }
          default:
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingrediente inválido.");
      }
  }

}