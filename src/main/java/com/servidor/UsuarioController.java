package com.servidor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private RepositorioUsuario usuarioRepository;

	@GetMapping
	public ResponseEntity<Object> verificarUsuario(@RequestParam String usuario, @RequestParam String senha) {
	    Optional<UsuarioLogin> optionalUsuario = usuarioRepository.findByUsuarioAndSenha(usuario, senha);

	    if (optionalUsuario.isPresent()) {
	        UsuarioLogin u = optionalUsuario.get();
	        Map<String, Object> response = new HashMap<>();
	        response.put("permissao", u.getPermissao());
	        return ResponseEntity.ok(response);
	    }

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("mensagem", "Usuário ou senha incorretos."));
	}
}
