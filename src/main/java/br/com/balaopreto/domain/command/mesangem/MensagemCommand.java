package br.com.balaopreto.domain.command.mesangem;

import br.com.balaopreto.adapter.input.dto.mensagem.*;
import br.com.balaopreto.domain.entity.Mensagem;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
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

        iMensagemRepositorio.marcarMensagensComoLidas(usuarioId, contatoId);

        List<Mensagem> mensagens = iMensagemRepositorio.listarMensagensEntreUsuarios(usuarioId, contatoId);

        return mensagens.stream()
                .map(m -> MensagemResponseDto.builder()
                        .conteudo(m.getConteudo())
                        .dataHora(m.getDatahora())
                        .enviadaPorMim(m.getDeId() == usuarioId)
                        .status(m.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<ConversaResumoResponseDto> listarConversasRecentes(String email) {
        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(email);
        return iMensagemRepositorio.listarConversasRecentes(usuarioId);
    }

    @Override
    public List<MensagemResponseDto> buscarNovasMensagens(String emailUsuario, String telefoneContato) {

        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        int contatoId = iUsuarioRepositorio.buscarIdPorTelefone(telefoneContato);

        List<Mensagem> mensagens = iMensagemRepositorio.buscarMensagensNaoLidas(contatoId, usuarioId);

        return mensagens.stream().map(m -> MensagemResponseDto.builder()
                        .conteudo(m.getConteudo())
                        .dataHora(m.getDatahora())
                        .enviadaPorMim(false)
                        .status(m.getStatus())
                        .build())
                        .toList();
    }

    public List<ConversaResponseDto> listaConversasRecentes(String emailUsuario) {

        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        List<Mensagem> ultimas = iMensagemRepositorio.listarUltimaMensagemPorConversa(usuarioId);

        return ultimas.stream().map(msg -> {
            int outroId = msg.getDeId() == usuarioId ? msg.getParaId() : msg.getDeId();
            var contato = iUsuarioRepositorio.buscarNomeETelefonePorId(outroId);

            return ConversaResponseDto.builder()
                    .nomeContato(contato.getNome())
                    .telefoneContato(contato.getTelefone())
                    .conteudoUltimaMensagem(msg.getConteudo())
                    .dataHora(msg.getDatahora())
                    .status(msg.getStatus())
                    .enviadaPorMim(msg.getDeId() == usuarioId)
                    .build();
        }).toList();
    }

    @Override
    public void deletarMensagem(String emailUsuario, DeletarMensagemRequestDto request) {
        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        Mensagem mensagem = iMensagemRepositorio.buscarMensagemPorId(request.getMensagemId());

        if (mensagem == null) {
            throw new CustomException("Mensagem não encontrada.");
        }

        boolean souRemetente = mensagem.getDeId() == usuarioId;
        boolean souDestinatario = mensagem.getParaId() == usuarioId;

        if (!souRemetente && !souDestinatario) {
            throw new CustomException("Você não tem permissão para essa ação.");
        }

        String tipo = request.getTipoExclusao().toUpperCase();

        switch (tipo) {
            case "PARA_TODOS" -> {
                if (!souRemetente) {
                    throw new CustomException("Apenas o remetente pode excluir para todos.");
                }
                iMensagemRepositorio.atualizarVisibilidadeMensagem(mensagem.getId(), false, false);
            }

            case "PARA_MIM" -> {
                boolean mensagemParaMimMesmo = mensagem.getDeId() == mensagem.getParaId();
                if (mensagemParaMimMesmo && souRemetente) {
                    // Apaga para ambos se for mensagem para si mesmo
                    iMensagemRepositorio.atualizarVisibilidadeMensagem(mensagem.getId(), false, false);
                } else if (souRemetente) {
                    // Apaga só do lado do remetente
                    iMensagemRepositorio.atualizarVisibilidadeMensagem(mensagem.getId(), false, null);
                } else if (souDestinatario) {
                    // Apaga só do lado do destinatário
                    iMensagemRepositorio.atualizarVisibilidadeMensagem(mensagem.getId(), null, false);
                }
            }

            default -> throw new CustomException("Tipo de exclusão inválido.");
        }
    }
}