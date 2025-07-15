package br.com.balaopreto.adapter.input.mesagem;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.adapter.input.dto.mensagem.DeletarMensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemResponseDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.port.input.IMensagemCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/mensagem")
public class MensagemController implements IMensagemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MensagemController.class);

    private final IMensagemCommand iMensagemCommand;
    private final JwtUtils jwtUtils;

    public MensagemController(IMensagemCommand iMensagemCommand, JwtUtils jwtUtils) {
        this.iMensagemCommand = iMensagemCommand;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Envia uma nova mensagem para um contato específico.
     *
     * @param token  Token JWT de autorização (Bearer Token).
     * @param request DTO contendo os dados da mensagem a ser enviada.
     * @return ResponseEntity com mensagem de sucesso após enviar a mensagem.
     */
    @PostMapping("/enviar")
    public ResponseEntity<StandardResponseDto> enviarMensagem(
            @RequestHeader("Authorization") String token,
            @RequestBody MensagemRequestDto request) {

        LOGGER.info("Início do método para enviar mensagem - Controller");

        String email = jwtUtils.extrairEmail(token.substring(7));
        iMensagemCommand.enviarMensagem(email, request);

        return ResponseEntity.ok(StandardResponseDto.builder().message("Mensagem enviada com sucesso!").build());
    }

    /**
     * Lista todas as mensagens trocadas entre o usuário logado e um contato específico.
     *
     * @param token           Token JWT de autorização (Bearer Token).
     * @param telefoneContato Telefone do contato cujas mensagens serão buscadas.
     * @return ResponseEntity com uma lista de mensagens trocadas.
     */
    @GetMapping("/conversa")
    public ResponseEntity<List<MensagemResponseDto>> listarMensagens(
            @RequestHeader("Authorization") String token,
            @RequestParam String telefoneContato) {

        LOGGER.info("Início do método para listar mensagens com contato - Controller");

        String email = jwtUtils.extrairEmail(token.substring(7));
        List<MensagemResponseDto> mensagens = iMensagemCommand.listarMensagens(email, telefoneContato);

        return ResponseEntity.ok(mensagens);
    }

    /**
     * Lista as conversas mais recentes do usuário logado com seus contatos.
     *
     * @param token Token JWT de autorização (Bearer Token).
     * @return ResponseEntity com uma lista resumida das conversas mais recentes.
     */
    @GetMapping("/conversas-recentes")
    public ResponseEntity<List<ConversaResumoResponseDto>> listarConversasRecentes(
            @RequestHeader("Authorization") String token) {

        LOGGER.info("Início do método para listar conversas recentes - Controller");

        String email = jwtUtils.extrairEmail(token.substring(7));
        List<ConversaResumoResponseDto> conversas = iMensagemCommand.listarConversasRecentes(email);

        return ResponseEntity.ok(conversas);
    }

    /**
     * Verifica e retorna as novas mensagens não lidas de um contato específico.
     *
     * @param token           Token JWT de autorização (Bearer Token).
     * @param telefoneContato Telefone do contato cujas mensagens não lidas serão buscadas.
     * @return ResponseEntity com uma lista de novas mensagens não lidas.
     */
    @GetMapping("/novas")
    public ResponseEntity<List<MensagemResponseDto>> verificarNovasMensagens(
            @RequestHeader("Authorization") String token,
            @RequestParam String telefoneContato) {

        LOGGER.info("Início do método para buscar novas mensagens - Controller");

        String email = jwtUtils.extrairEmail(token.substring(7));
        List<MensagemResponseDto> novasMensagens = iMensagemCommand.buscarNovasMensagens(email, telefoneContato);

        return ResponseEntity.ok(novasMensagens);
    }

    /**
     * Apaga uma mensagem específica do histórico do usuário.
     *
     * @param token  Token JWT de autorização (Bearer Token).
     * @param request DTO contendo o identificador da mensagem a ser apagada.
     * @return ResponseEntity com mensagem de confirmação da exclusão.
     */
    @DeleteMapping("/deletar")
    public ResponseEntity<StandardResponseDto> deletarMensagem(@RequestHeader("Authorization") String token, @RequestBody DeletarMensagemRequestDto request) {

        LOGGER.info("Início do método para deletar mensagem - Controller");

        String email = jwtUtils.extrairEmail(token.substring(7));
        iMensagemCommand.deletarMensagem(email, request);
        return ResponseEntity.ok(StandardResponseDto.builder().message("Mensagem apagada.").build());
    }
}