package br.com.balaopreto.adapter.input.dto.mensagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversaResponseDto {
    private String nomeContato;
    private String telefoneContato;
    private String conteudoUltimaMensagem;
    private String dataHora;
    private String status;
    private boolean enviadaPorMim;
}
