package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;

import java.util.List;

public interface IVerificarCodigoRepository {
    void salvarCodigoVerificacao(String nome, String telefone, String email, int codigo);
    List<Integer> autenticarUsuario();
    List<UsuarioRequestDto> salvarDadosDoUsuario();
}
