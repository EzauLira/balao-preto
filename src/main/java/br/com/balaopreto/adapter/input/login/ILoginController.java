package br.com.balaopreto.adapter.input.login;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;
import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ILoginController {

    ResponseEntity<StandardResponseDto> logar(@RequestBody UsuarioRequestDto request);
    ResponseEntity<TokenResponseDto> autenticarUsuario(@RequestParam int codigo);
}
