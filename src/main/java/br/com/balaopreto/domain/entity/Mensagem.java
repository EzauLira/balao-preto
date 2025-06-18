package br.com.balaopreto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {
    private long id;
    private long deId;
    private long paraId;
    private String datahora;
    private String estatus;
    private Contato contato;

}
