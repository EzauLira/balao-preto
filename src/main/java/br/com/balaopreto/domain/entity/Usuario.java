package br.com.balaopreto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private long id;
    private String nome;
    private String email;
    private String senha;
    private String dataCadastro;
    private double telefone;
}
