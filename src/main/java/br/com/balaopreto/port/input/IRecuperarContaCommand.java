package br.com.balaopreto.port.input;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;

public interface IRecuperarContaCommand {

    String recuperarConta(String telefone);
    TokenResponseDto autenticarConta(int codigo);
}
