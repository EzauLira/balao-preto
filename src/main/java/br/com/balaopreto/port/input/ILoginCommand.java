package br.com.balaopreto.port.input;


public interface ILoginCommand {

    void iniciarLogin(String email);

    void autenticarUsuario(int codigo);
}
