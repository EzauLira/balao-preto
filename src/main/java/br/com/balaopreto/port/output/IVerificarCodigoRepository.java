package br.com.balaopreto.port.output;

import java.util.List;

public interface IVerificarCodigoRepository {
    void salvarCodigoVerificacao(String email, int codigo);
    List<Integer> autenticarUsuario();

}
