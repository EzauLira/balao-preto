package br.com.balaopreto.port.input;

public interface IRecuperarContaCommand {

    String recuperarConta(String telefone);
    void autenticarConta(int codigo);
}
