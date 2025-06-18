package br.com.balaopreto.adapter.input.verificarCodigo;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.VerificacaoCodigoDTO;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IVerificarCodigoController {
    ResponseEntity<StandardResponseDto> verificarTelefone(@RequestBody VerificacaoCodigoDTO verificacaoCodigoDTO);
    ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo);

}
