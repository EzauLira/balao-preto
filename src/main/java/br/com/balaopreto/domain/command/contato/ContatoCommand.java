package br.com.balaopreto.domain.command.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.adapter.input.dto.contato.ListaContatosResponseDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IContatoCommand;
import br.com.balaopreto.port.output.IContatoRepositorio;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoCommand implements IContatoCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContatoCommand.class);
    private final IContatoRepositorio iContatoRepositorio;
    private final IUsuarioRepositorio iUsuarioRepositorio;
    private final JwtUtils jwtUtils;

    public ContatoCommand(IContatoRepositorio iContatoRepositorio, IUsuarioRepositorio iUsuarioRepositorio, JwtUtils jwtUtils) {
        this.iContatoRepositorio = iContatoRepositorio;
        this.jwtUtils = jwtUtils;
        this.iUsuarioRepositorio = iUsuarioRepositorio;
    }


    /**
     * Adiciona um novo contato para o usuário identificado pelo e-mail.
     * Verifica se o usuário não está tentando adicionar a si mesmo,
     * se o telefone do contato existe no sistema e se o contato já foi adicionado anteriormente.
     *
     * @param emailUsuario O e-mail do usuário que está adicionando o contato.
     * @param request      DTO contendo os dados do contato (telefone e apelido).
     */
    @Override
    public void adicionarContato(String emailUsuario, ContatoRequestDto request) {
        LOGGER.info("Início do método para adicionar um contato ao usuário - Serviço");

            int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
            int contatoId = iUsuarioRepositorio.buscarIdPorTelefone(request.getTelefone());

            if (usuarioId == contatoId) {
                throw new CustomException("Você não pode se adicionar como contato.");
            }

            if (!iUsuarioRepositorio.existeTelefone(request.getTelefone())) {
                throw new CustomException("Telefone informado não está vinculado a nenhuma conta.");
            }

            if (iContatoRepositorio.contatoJaExiste(usuarioId, contatoId)) {
                throw new CustomException("Contato já adicionado.");
            }

            iContatoRepositorio.adicionarContato(usuarioId, contatoId, request.getApelido());
    }

    /**
     * Lista todos os contatos do usuário logado.
     *
     * @param emailUsuario O e-mail do usuário cujos contatos serão listados.
     * @return Uma lista de DTOs contendo informações dos contatos do usuário.
     */
    @Override
    public List<ListaContatosResponseDto> listarContatos(String emailUsuario) {
        LOGGER.info("Início do método listarContatos - Serviço");

        int usuarioId = iUsuarioRepositorio.buscarIdPorEmail(emailUsuario);
        return iContatoRepositorio.listarContatos(usuarioId);
    }
}