package br.com.balaopreto.adapter.input.verificarCodigo;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.VerificacaoCodigoDTO;
import br.com.balaopreto.config.dto.StandardResponseDto;
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

    @PostMapping("/enviar-codigo")
    public ResponseEntity<StandardResponseDto> verificarTelefone(@RequestBody VerificacaoCodigoDTO verificacaoCodigoDTO) {
        LOGGER.info("Início do método para verificar o email - controller");

        iVerificarCodigoCommand.salvarCodigoVerificacao(verificacaoCodigoDTO.getEmail());

        return ResponseEntity.ok(StandardResponseDto.builder().message("Código enviado para o email informado.").build());
    }

    @GetMapping("/verificar-codigo")
    public ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo){
        LOGGER.info("Início do método para verificar o código - controller");

        iVerificarCodigoCommand.autenticarUsuario(codigo);

        return ResponseEntity.ok(StandardResponseDto.builder().message("Verificação Concluida!").build());
    }
}
