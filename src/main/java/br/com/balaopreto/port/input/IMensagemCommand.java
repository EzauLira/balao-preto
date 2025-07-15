package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.mensagem.*;

import java.util.List;

public interface IMensagemCommand {

    void enviarMensagem(String emailUsuario, MensagemRequestDto request);
    List<MensagemResponseDto> listarMensagens(String emailUsuario, String telefoneContato);
    List<ConversaResumoResponseDto> listarConversasRecentes(String email);
    List<MensagemResponseDto> buscarNovasMensagens (String emailUsuario, String telefoneContato);
    List<ConversaResponseDto> listaConversasRecentes(String emailUsuario);
    void deletarMensagem(String emailUsuario, DeletarMensagemRequestDto request);
}
