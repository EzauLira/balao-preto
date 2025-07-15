package br.com.balaopreto.adapter.input.dto.mensagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletarMensagemRequestDto {

    private int mensagemId;
    private String tipoExclusao;

}
