package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.domain.entity.Mensagem;

import java.util.List;

public interface IMensagemRepositorio {

    void salvarMensagem(Mensagem request);
    List<Mensagem> listarMensagensEntreUsuarios(int usuarioId, int contatoId);

    List<ConversaResumoResponseDto> listarConversasRecentes(int usuarioId);
}
