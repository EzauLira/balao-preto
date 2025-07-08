package br.com.balaopreto.port.input;


import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;

public interface ILoginCommand {

    void iniciarLogin(String email);

    TokenResponseDto autenticarUsuario(int codigo);
}
