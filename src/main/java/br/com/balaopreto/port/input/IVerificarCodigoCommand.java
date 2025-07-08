package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;
import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

public interface IVerificarCodigoCommand {

//    void salvarCodigoVerificacao(String email);
    TokenResponseDto autenticarUsuario(int codigo);
    void confirmarUsuarioPorEmail(UsuarioRequestDto usuarioRequestDto);
}
