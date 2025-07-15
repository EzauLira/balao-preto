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
    private int id;
    private int deId;
    private int paraId;
    private String datahora;
    private String status;
    private Contato contato;
    private String conteudo;
    private boolean enviadaPorMim;
    private boolean visivelParaRemetente;
    private boolean visivelParaDestinatario;

}
