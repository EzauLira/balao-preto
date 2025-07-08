package br.com.balaopreto.domain.command.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.adapter.input.dto.contato.ContatoResponseDto;
import br.com.balaopreto.port.input.IContatoCommand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoCommand implements IContatoCommand {


    @Override
    public void adicionarContato(int idUsuario, ContatoRequestDto dto) {

    }

    @Override
    public List<ContatoResponseDto> listarContatos(int idUsuario) {
        return null;
    }
}
