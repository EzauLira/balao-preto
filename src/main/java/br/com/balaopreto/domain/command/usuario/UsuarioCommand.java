package br.com.balaopreto.domain.command.usuario;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.domain.entity.Usuario;
import br.com.balaopreto.port.input.IUsuarioCommand;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UsuarioCommand implements IUsuarioCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioCommand.class);

    private IUsuarioRepositorio iUsuarioRepositorio;

    public UsuarioCommand(IUsuarioRepositorio iUsuarioRepositorio){
        this.iUsuarioRepositorio = iUsuarioRepositorio;
    }

    @Override
    public void registrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        LOGGER.info("Início do método para registrar o usuário - Service.");

            var usuario = new Usuario();
            usuario.setNome(usuarioRequestDto.getNome());
            usuario.setEmail(usuarioRequestDto.getEmail());
            usuario.setSenha(usuarioRequestDto.getSenha());
            usuario.setDataCadastro(usuarioRequestDto.getDataCadastro());
            usuario.setTelefone(usuarioRequestDto.getTelefone());

            LOGGER.info("Entrando no método Repositório - Service ");
            iUsuarioRepositorio.registrarUsuario(usuario);
    }
}