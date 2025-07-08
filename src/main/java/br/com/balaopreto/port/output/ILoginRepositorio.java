package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;

import java.util.List;
public interface ILoginRepositorio {

    void salvarCodigoAutenticacao(String email, int codigo);

    List<CodigoEmailDto> autenticarUsuario();

    boolean existeEmail(String email);

    List<String> extrairEmailVerificacao();

    long buscarIdPorEmail(String email);
}
