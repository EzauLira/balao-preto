package br.com.balaopreto.adapter.input.mesagem;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IMensagemController {

    ResponseEntity<String> enviarMensagem(
            @RequestHeader("Authorization") String token,
            @RequestBody MensagemRequestDto request);

    ResponseEntity<List<MensagemResponseDto>> listarMensagens(
            @RequestHeader("Authorization") String token,
            @RequestParam String telefoneContato);

    ResponseEntity<List<ConversaResumoResponseDto>> listarConversasRecentes(
            @RequestHeader("Authorization") String token);
}
