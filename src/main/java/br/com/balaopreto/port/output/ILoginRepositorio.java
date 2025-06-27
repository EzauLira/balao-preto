package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

import java.util.List;

public interface ILoginRepositorio {

    void salvarCodigoAutenticacao(UsuarioRequestDto request, int codigo);

    List<Integer> autenticarUsuario();
}
