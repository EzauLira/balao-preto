package br.com.balaopreto.adapter.input.verificarCodigo;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;
import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import br.com.balaopreto.port.input.IVerificarCodigoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/verificacao")
public class VerificarCodigoController implements IVerificarCodigoController{

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificarCodigoController.class);
    @Autowired
    private final IVerificarCodigoCommand iVerificarCodigoCommand;

    public VerificarCodigoController(IVerificarCodigoCommand iVerificarCodigoCommand) {
        this.iVerificarCodigoCommand = iVerificarCodigoCommand;
    }

    /**
     * Recebe através da requisição os dados do usuário e envia para service para gerar um código, em seguida enviar um e-mail.
     * Retorna 200 mensagem de sucesso.
     * @param usuarioRequestDto
     * @return
     */
    @Override
    @PostMapping("/enviar-dados")
    public ResponseEntity<StandardResponseDto> confirmarUsuarioPorEmail(@RequestBody UsuarioRequestDto usuarioRequestDto){
        LOGGER.info("Início do método para validar um novo usuário - controller");

        iVerificarCodigoCommand.confirmarUsuarioPorEmail(usuarioRequestDto);

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.CODIGO_ENVIADO).build());
    }

    /**
     * Recebe através da requisição o código digitado pelo usuário e manda para service verificar se baco com o do banco.
     * Retorna 200 de código confirmado.
     * @param codigo
     * @return
     */
    @Override
    @GetMapping("/verificar-codigo")
    public ResponseEntity<TokenResponseDto> autenticarUsuario(@RequestParam int codigo){
        LOGGER.info("Início do método para verificar o código - controller");

        TokenResponseDto token = iVerificarCodigoCommand.autenticarUsuario(codigo);

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}