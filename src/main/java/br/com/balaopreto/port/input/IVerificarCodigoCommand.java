package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

public interface IVerificarCodigoCommand {

//    void salvarCodigoVerificacao(String email);
    void autenticarUsuario(int codigo);
    void confirmarUsuarioPorEmail(UsuarioRequestDto usuarioRequestDto);
}
