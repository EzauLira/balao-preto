package br.com.balaopreto.port.output;

import br.com.balaopreto.domain.entity.Usuario;

import java.util.List;

public interface IUsuarioRepositorio {

    void registrarUsuario(Usuario usuario);
    boolean consultaUsuarior(String email);
}
