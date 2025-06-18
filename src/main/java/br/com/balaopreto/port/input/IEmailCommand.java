package br.com.balaopreto.port.input;

public interface IEmailCommand {

    void enviarEmail(String email, int codigo);

}