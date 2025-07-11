package br.com.balaopreto.domain.command.mesangem;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemRequestDto;
import br.com.balaopreto.adapter.input.dto.mensagem.MensagemResponseDto;
import br.com.balaopreto.domain.entity.Mensagem;
import br.com.balaopreto.port.input.IMensagemCommand;
import br.com.balaopreto.port.output.IMensagemRepositorio;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemCommand implements IMensagemCommand {

    private final IMensagemRepositorio iMensagemRepositorio;
    private final IUsuarioRepositorio iUsuarioRepositorio;

    public MensagemCommand(IMensagemRepositorio iMensagemRepositorio, IUsuarioRepositorio iUsuarioRepositorio) {
        this.iMensagemRepositorio = iMensagemRepositorio;
        this.iUsuarioRepositorio = iUsuarioRepositorio;
    }

    @Override
    public void enviarMensagem(String emailUsuario, MensagemRequestDto request) {
        int remetenteId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        int destinatarioId = iUsuarioRepositorio.buscarIdPorTelefone(request.getTelefone());

        Mensagem mensagem = Mensagem.builder()
                .deId(remetenteId)
                .paraId(destinatarioId)
                .conteudo(request.getConteudo())
                .status("ENVIADA")
                .build();

        iMensagemRepositorio.salvarMensagem(mensagem);
    }

    @Override
    public List<MensagemResponseDto> listarMensagens(String emailUsuario, String telefoneContato) {
        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        int contatoId = iUsuarioRepositorio.buscarIdPorTelefone(telefoneContato);

        List<Mensagem> mensagens = iMensagemRepositorio.listarMensagensEntreUsuarios(usuarioId, contatoId);

        return mensagens.stream()
                .map(m -> MensagemResponseDto.builder()
                        .conteudo(m.getConteudo())
                        .dataHora(m.getDatahora())
                        .enviadaPorMim(m.getDeId() == usuarioId) // Aqui identifica o autor da mensagem
                        .build())
                .toList();
    }

    @Override
    public List<ConversaResumoResponseDto> listarConversasRecentes(String email) {
        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(email);
        return iMensagemRepositorio.listarConversasRecentes(usuarioId);
    }

}