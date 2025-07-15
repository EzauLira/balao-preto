package br.com.balaopreto.port.input;


import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.adapter.input.dto.contato.ContatoResponseDto;
import br.com.balaopreto.adapter.input.dto.contato.ListaContatosResponseDto;

import java.util.List;

public interface IContatoCommand {

    void adicionarContato(String emailUsuario, ContatoRequestDto request);
    List<ListaContatosResponseDto> listarContatos(String emailUsuario);
}
