package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

public interface ILoginCommand {

    void logar(UsuarioRequestDto request);

    void autenticarUsuario(int codigo);
}
