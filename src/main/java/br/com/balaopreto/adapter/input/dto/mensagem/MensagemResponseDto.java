package br.com.balaopreto.adapter.input.dto.mensagem;

import br.com.balaopreto.domain.entity.Contato;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponseDto {
    private long id;
    private long deId;
    private long paraId;
    private String datahora;
    private String estatus;
    private Contato contato;
}
