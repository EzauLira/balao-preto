package br.com.balaopreto.adapter.input.recuperarConta;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.port.input.IRecuperarContaCommand;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/recuperar")
public class RecuperarContaController implements IRecuperarContaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecuperarContaController.class);
    @Autowired
    private final IRecuperarContaCommand iRecuperarContaCommand;

    public RecuperarContaController(IRecuperarContaCommand iRecuperarContaCommand) {
        this.iRecuperarContaCommand = iRecuperarContaCommand;
    }

    /**
     * Recebe através da requisição os dados do usuário e envia para service para gerar um código, em seguida enviar um e-mail.
     * Retorna 200 mensagem de sucesso.
     * @param usuarioRequestDto
     * @return
     */
    @Override
    @PostMapping("/email-por-telefone")
    public ResponseEntity<StandardResponseDto> recuperarConta(@RequestBody UsuarioRequestDto usuarioRequestDto){
        LOGGER.info("Início do método para validar um novo usuário - controller");

        String emailColetado = iRecuperarContaCommand.recuperarConta(usuarioRequestDto.getTelefone());

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.CODIGO_ENVIADO_RECUPERACAO + emailColetado).build());
    }

    @Override
    @GetMapping("/verificar-codigo")
    public ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo){
        LOGGER.info("Início do método para verificar o código - controller");

        iRecuperarContaCommand.autenticarConta(codigo);

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.USUARIO_LOGADO).build());
    }
}