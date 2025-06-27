package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

public interface IUsuarioCommand {

    void registrarUsuario(UsuarioRequestDto usuarioRequestDto);
}
