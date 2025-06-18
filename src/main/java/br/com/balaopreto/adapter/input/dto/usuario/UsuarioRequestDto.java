package br.com.balaopreto.adapter.input.dto.usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioRequestDto {

    private long id;
    private String nome;
    private String email;
    private String senha;
    private String dataCadastro;
    private double telefone;

}
