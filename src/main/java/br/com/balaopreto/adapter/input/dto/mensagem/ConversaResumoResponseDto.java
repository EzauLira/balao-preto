package br.com.balaopreto.adapter.input.dto.mensagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversaResumoResponseDto {

    private String nomeContato;
    private String telefoneContato;
    private String ultimaMensagem;
    private String dataHora;
    private boolean mensagemMinha;
}
