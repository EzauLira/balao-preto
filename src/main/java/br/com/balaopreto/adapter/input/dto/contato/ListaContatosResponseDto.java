package br.com.balaopreto.adapter.input.dto.contato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListaContatosResponseDto {

    private String apelido;
    private String telefone;
    //private String descricao;
}
