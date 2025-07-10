package br.com.balaopreto.port.output;

import br.com.balaopreto.domain.entity.Usuario;

public interface IUsuarioRepositorio {

    void registrarUsuario(Usuario usuario);
    boolean consultaUsuarior(String email);
    int buscarIdPorEmail(String email);
    int buscarIdPorTelefone(String telefone);
    boolean existeTelefone(String telefone);
}
