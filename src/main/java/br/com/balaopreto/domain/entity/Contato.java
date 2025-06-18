package br.com.balaopreto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contato {
    private long id;
    private long usuarioId;
    private long contatoId;
    private long apelido;

}
