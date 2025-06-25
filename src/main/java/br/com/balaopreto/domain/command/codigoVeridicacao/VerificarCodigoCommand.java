package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IEmailCommand;
import br.com.balaopreto.port.input.IUsuarioCommand;
import br.com.balaopreto.port.input.IVerificarCodigoCommand;
import br.com.balaopreto.port.output.IVerificarCodigoRepository;
import br.com.balaopreto.utils.CodigoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificarCodigoCommand implements IVerificarCodigoCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificarCodigoCommand.class);

    private final IVerificarCodigoRepository iVerificarCodigoRepository;
    private IUsuarioCommand iUsuarioCommand;
    private final IEmailCommand iEmailCommand;

    public VerificarCodigoCommand(IVerificarCodigoRepository iVerificarCodigoRepository,IUsuarioCommand iUsuarioCommand, IEmailCommand iEmailCommand) {
        this.iVerificarCodigoRepository = iVerificarCodigoRepository;
        this.iUsuarioCommand = iUsuarioCommand;
        this.iEmailCommand = iEmailCommand;
    }

    public void confirmarUsuarioPorEmail(UsuarioRequestDto request) {
        LOGGER.info("Início do método para validar o usuário - Service.");

        var codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service ");
        iVerificarCodigoRepository.salvarCodigoVerificacao(request.getNome(), request.getTelefone(), request.getEmail(), codigo);

        iEmailCommand.enviarEmail(request.getEmail(), codigo);
    }

    @Override
    public void autenticarUsuario(int codigo) {

        LOGGER.info("Início do método para verificar se o código existe - Service.");

        List<Integer> codigoBanco = iVerificarCodigoRepository.autenticarUsuario();

        List<UsuarioRequestDto> pegarDados = iVerificarCodigoRepository.salvarDadosDoUsuario();

        for (int lista : codigoBanco) {
            if (codigo == lista) {
                for (UsuarioRequestDto dadosUsuario : pegarDados){

                    iUsuarioCommand.registrarUsuario(dadosUsuario);
                }
                return;
            }
        }
        throw new CustomException(MensagensUtils.CODIGO_INVALIDO_OU_INCORRETO);
    }
}