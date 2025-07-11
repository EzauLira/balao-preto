package br.com.balaopreto.adapter.input.dto.mensagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponseDto {
    private String conteudo;
    private String dataHora;
    private boolean enviadaPorMim;
    private String status;
}

