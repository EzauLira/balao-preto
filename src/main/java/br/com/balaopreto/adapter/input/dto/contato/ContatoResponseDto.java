package br.com.balaopreto.adapter.input.dto.contato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContatoResponseDto {

    private int id;
    private int contatoId;
    private String apelido;
    private LocalDateTime dataAdicao;
}
