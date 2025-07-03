package br.com.balaopreto.adapter.input.login;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.port.input.ILoginCommand;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/login")
public class LoginController implements ILoginController {

    private final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final ILoginCommand iLoginCommand;

    public LoginController(ILoginCommand iLoginCommand) {
        this.iLoginCommand = iLoginCommand;
    }

    /**
     * Recebe através da requisição os dados do usuário e envia para service para gerar um código, em seguida enviar um e-mail.
     * Retorna 200 mensagem de sucesso.
     * @param request
     * @return
     */
    @Override
    @PostMapping("/logar")
    public ResponseEntity<StandardResponseDto> logar(@RequestBody UsuarioRequestDto request) {

        LOGGER.info("Início do método para logar - controller");

        iLoginCommand.iniciarLogin(request.getEmail());

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.SUCESSO_CODIGO_ENVIADO).build());
    }

    /**
     * Recebe através da requisição o código digitado pelo usuário e manda para service verificar se baco com o do banco.
     * Retorna 200 de código confirmado.
     * @param codigo
     * @return
     */
    @Override
    @GetMapping("/verificar-codigo")
    public ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo){
        LOGGER.info("Início do método para verificar o código - controller");

        iLoginCommand.autenticarUsuario(codigo);

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.USUARIO_LOGADO).build());
    }
}
