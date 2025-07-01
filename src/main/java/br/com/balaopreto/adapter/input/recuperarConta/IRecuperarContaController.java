package br.com.balaopreto.adapter.input.recuperarConta;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IRecuperarContaController {

    ResponseEntity<StandardResponseDto> recuperarConta(@RequestBody UsuarioRequestDto usuarioRequestDto);
    ResponseEntity<StandardResponseDto> autenticarUsuario(@RequestParam int codigo);
}
