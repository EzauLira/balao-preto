package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemResponseDto;

import java.util.List;

public interface IMensagemCommand {

    void enviarMensagem(String emailUsuario, MensagemRequestDto request);
    List<MensagemResponseDto> listarMensagens(String emailUsuario, String telefoneContato);
    List<ConversaResumoResponseDto> listarConversasRecentes(String email);
}
