package br.com.balaopreto.port.input;

public interface IVerificarCodigoCommand {

    void salvarCodigoVerificacao(String email);
    void autenticarUsuario(int codigo);
}
