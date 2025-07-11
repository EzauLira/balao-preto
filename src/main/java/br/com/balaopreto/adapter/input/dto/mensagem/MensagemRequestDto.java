package br.com.balaopreto.adapter.input.dto.mensagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensagemRequestDto {
    private int deId;
    private int paraId;
    private String conteudo;
    private String status;
    private String contato;
    private String telefone;
}
