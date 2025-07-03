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

    private String nome;
    private String telefone;
    private String email;

   public UsuarioRequestDto(String email){
        this.email = email;
    }
}
