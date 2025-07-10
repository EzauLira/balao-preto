package br.com.balaopreto.adapter.input.dto.contato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContatoRequestDto {

    private String telefone;
    private String apelido;
}
