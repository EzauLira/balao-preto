package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;

public interface IUsuarioCommand {

    void registrarUsuario(UsuarioRequestDto usuarioRequestDto);
}
