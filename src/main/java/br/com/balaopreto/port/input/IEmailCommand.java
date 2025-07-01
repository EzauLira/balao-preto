package br.com.balaopreto.port.input;

public interface IEmailCommand {

    void enviarEmailVerificacao(String email, int codigo);
    void enviarEmailAutenticacao(String email, int codigo);

    void enviarEmailRecuperacao(String email, int codigo);

}