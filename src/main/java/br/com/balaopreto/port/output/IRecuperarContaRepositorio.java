package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;

import java.util.List;

public interface IRecuperarContaRepositorio {

    void salvarCodigoAutenticacao(String email, int codigo);
    List<String> extrairEmail(String telefone);
    List<CodigoEmailDto> autenticarConta();
}