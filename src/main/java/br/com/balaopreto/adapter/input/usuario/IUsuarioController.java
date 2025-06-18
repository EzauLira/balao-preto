package br.com.balaopreto.adapter.input.usuario;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUsuarioController {

    ResponseEntity<StandardResponseDto> registrarUsuario(@RequestBody UsuarioRequestDto usuarioRequestDto);
}
