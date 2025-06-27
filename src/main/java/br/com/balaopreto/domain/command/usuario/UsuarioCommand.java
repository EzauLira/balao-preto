package br.com.balaopreto.domain.command.usuario;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.domain.entity.Usuario;
import br.com.balaopreto.port.input.IUsuarioCommand;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import br.com.balaopreto.utils.validadores.ValidarEmailUtils;
import br.com.balaopreto.utils.validadores.ValidarTelefoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UsuarioCommand implements IUsuarioCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioCommand.class);

    private IUsuarioRepositorio iUsuarioRepositorio;

    public UsuarioCommand(IUsuarioRepositorio iUsuarioRepositorio) {
        this.iUsuarioRepositorio = iUsuarioRepositorio;
    }

    /**
     * Esse método valida os dados vindos do usuário, monta o usuário com os dados vindos da requisição e envia para o repositório.
     * @param usuarioRequestDto
     */
    @Override
    public void registrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        LOGGER.info("Início do método para registrar o usuário - Service.");

        ValidarEmailUtils.validarEmail(usuarioRequestDto.getEmail());
        ValidarTelefoneUtils.validarTelefone(usuarioRequestDto.getTelefone());

        var usuario = new Usuario();
        usuario.setNome(usuarioRequestDto.getNome());
        usuario.setTelefone(usuarioRequestDto.getTelefone());
        usuario.setEmail(usuarioRequestDto.getEmail());

        LOGGER.info("Entrando no método Repositório - Service ");
        iUsuarioRepositorio.registrarUsuario(usuario);
    }
}