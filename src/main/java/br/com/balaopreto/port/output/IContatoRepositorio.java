package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.contato.ContatoResponseDto;

import java.util.List;

public interface IContatoRepositorio {

    void adicionarContato(int usuarioId, int contatoId, String apelido);

    boolean contatoJaExiste(int usuarioId, int contatoId);

    List<ContatoResponseDto> listarContatos(int usuarioId);

}
