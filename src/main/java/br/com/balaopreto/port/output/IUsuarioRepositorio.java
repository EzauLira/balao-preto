package br.com.balaopreto.port.output;

import br.com.balaopreto.domain.entity.Usuario;

public interface IUsuarioRepositorio {

    void registrarUsuario(Usuario usuario);
    boolean consultaUsuarior(String email);
    long buscarIdPorEmail(String email);
}
