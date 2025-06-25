package br.com.balaopreto.adapter.input.verificarCodigo;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import br.com.balaopreto.port.input.IVerificarCodigoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @PostMapping("/enviar-dados")
    public ResponseEntity<StandardResponseDto> confirmarUsuarioPorEmail(@RequestBody UsuarioRequestDto usuarioRequestDto){
        LOGGER.info("Início do método para validar um novo usuário - controller");

        iVerificarCodigoCommand.confirmarUsuarioPorEmail(usuarioRequestDto);

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.CODIGO_ENVIADO).build());
    }

    @GetMapping("/verificar-codigo")
    public ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo){
        LOGGER.info("Início do método para verificar o código - controller");

        iVerificarCodigoCommand.autenticarUsuario(codigo);

        return ResponseEntity.ok(StandardResponseDto.builder().message(MensagensUtils.VERIFICACAO_CONCLUIDA).build());
    }
}