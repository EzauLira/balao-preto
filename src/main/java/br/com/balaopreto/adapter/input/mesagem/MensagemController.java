package br.com.balaopreto.adapter.input.mesagem;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemResponseDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.port.input.IMensagemCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/mensagem")
public class MensagemController implements IMensagemController {

    private final IMensagemCommand iMensagemCommand;
    private final JwtUtils jwtUtils;

    public MensagemController(IMensagemCommand iMensagemCommand, JwtUtils jwtUtils) {
        this.iMensagemCommand = iMensagemCommand;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarMensagem(
            @RequestHeader("Authorization") String token,
            @RequestBody MensagemRequestDto request) {

        String email = jwtUtils.extrairEmail(token.substring(7));
        iMensagemCommand.enviarMensagem(email, request);

        return ResponseEntity.ok("Mensagem enviada com sucesso!");
    }

    @GetMapping("/conversa")
    public ResponseEntity<List<MensagemResponseDto>> listarMensagens(
            @RequestHeader("Authorization") String token,
            @RequestParam String telefoneContato) {

        String email = jwtUtils.extrairEmail(token.substring(7));
        List<MensagemResponseDto> mensagens = iMensagemCommand.listarMensagens(email, telefoneContato);

        return ResponseEntity.ok(mensagens);
    }

    @GetMapping("/conversas-recentes")
    public ResponseEntity<List<ConversaResumoResponseDto>> listarConversasRecentes(
            @RequestHeader("Authorization") String token) {

        String email = jwtUtils.extrairEmail(token.substring(7));
        List<ConversaResumoResponseDto> conversas = iMensagemCommand.listarConversasRecentes(email);

        return ResponseEntity.ok(conversas);
    }

}