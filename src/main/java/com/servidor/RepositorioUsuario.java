package com.servidor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioUsuario extends JpaRepository<UsuarioLogin, Long> {

	   Optional<UsuarioLogin> findByUsuarioAndSenha(String usuario, String senha);
	}

