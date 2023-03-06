package com.servidor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class UsuarioLogin {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String usuario;

   private String senha;

   private Integer permissao;

   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

   public String getUsuario() {
       return usuario;
   }

   public void setUsuario(String usuario) {
       this.usuario = usuario;
   }

   public String getSenha() {
       return senha;
   }

   public void setSenha(String senha) {
       this.senha = senha;
   }

   public Integer getPermissao() {
       return permissao;
   }

   public void setPermissao(Integer permissao) {
       this.permissao = permissao;
   }
}

