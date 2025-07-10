package br.com.balaopreto.adapter.input.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.adapter.input.dto.contato.ContatoResponseDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.port.input.IContatoCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/contatos")
public class ContatoController implements IContatoController {
    private final JwtUtils jwtUtils;
    private final IContatoCommand iContatoCommand;
    public ContatoController(JwtUtils jwtUtils, IContatoCommand iContatoCommand){
        this.jwtUtils = jwtUtils;
        this.iContatoCommand = iContatoCommand;
    }

    /**
     * Endpoint para adicionar um novo contato ao usuário autenticado.
     *
     * Extrai o e-mail do usuário a partir do token JWT fornecido no cabeçalho Authorization.
     * Em seguida, chama o comando de domínio para realizar a adição do contato.
     *
     * @param request Objeto DTO contendo os dados do contato (telefone e apelido).
     * @param token   Token JWT de autorização do usuário (no formato "Bearer <token>").
     * @return        ResponseEntity com mensagem de sucesso ou exceção tratada.
     */
    @Override
    @PostMapping("/adcionar")
    public ResponseEntity<StandardResponseDto> adicionarContato(@RequestBody ContatoRequestDto request, @RequestHeader("Authorization") String token) {
        String emailUsuario = jwtUtils.extrairEmail(token.substring(7));
        iContatoCommand.adicionarContato(emailUsuario, request);
        return ResponseEntity.ok(StandardResponseDto.builder().message("Contato adicionado com sucesso").build());
    }

    /**
     * Endpoint para listar os contatos do usuário autenticado.
     *
     * Extrai o ID do usuário a partir do token JWT fornecido no cabeçalho Authorization
     * e retorna uma resposta com os dados dos contatos vinculados ao usuário.
     *
     * @param token Token JWT de autorização do usuário (no formato "Bearer <token>").
     * @return      ResponseEntity com status OK e a lista de contatos do usuário.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<ContatoResponseDto>> listarContatos(
            @RequestHeader("Authorization") String token
    ) {
        String emailUsuario = jwtUtils.extrairEmail(token.substring(7));
        List<ContatoResponseDto> contatos = iContatoCommand.listarContatos(emailUsuario);
        return ResponseEntity.ok(contatos);
    }
}