package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;
import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
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
    private final IUsuarioCommand iUsuarioCommand;
    private final IUsuarioRepositorio iUsuarioRepositorio;
    private final IEmailCommand iEmailCommand;
    private final JwtUtils jwtUtils;
    private String emailDigitado;

    public VerificarCodigoCommand(IVerificarCodigoRepository iVerificarCodigoRepository,
                                  IUsuarioCommand iUsuarioCommand,
                                  IUsuarioRepositorio iUsuarioRepositorio,
                                  IEmailCommand iEmailCommand,
                                  JwtUtils jwtUtils) {

        this.iVerificarCodigoRepository = iVerificarCodigoRepository;
        this.iUsuarioCommand = iUsuarioCommand;
        this.iUsuarioRepositorio = iUsuarioRepositorio;
        this.iEmailCommand = iEmailCommand;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Esse método recebe os dados do usuário, verifica se já existe no banco dados @consultaUsuarior, se sim não prossegue, se não ele gera
     * um código aleatório de quatro dígitos, esse código é enviado para o banco de dados pelo método: @salvarCodigoVerificacao.
     * Consulta a tabela de usuários para verificar se o e-mail passado já existe no banco de dados.
     * Em seguida envia para o e-mail do usuário o mesmo código através do método @enviarEmail.
     *
     * @param request contém os dados da requisição do usuário.
     */
    @Override
    public void confirmarUsuarioPorEmail(UsuarioRequestDto request) {
        LOGGER.info("Início do método para validar o usuário - Service.");

        emailDigitado = request.getEmail();

        if (iUsuarioRepositorio.consultaUsuarior(request.getEmail()))
            throw new CustomException(MensagensUtils.USUARIO_JA_CADASTRADO);


        var codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service ");
        iVerificarCodigoRepository.salvarCodigoVerificacao(request, codigo);

        iEmailCommand.enviarEmailVerificacao(request.getEmail(), codigo);
    }

    /**
     * Esse método tem como princípio validar o usuário/confirmar conta.
     * Ele pega o código que o usuário recebeu no e-mail em seguida verifica se no banco tem o mesmo código @autentiarUsuario.
     * Ele pega os dados do usuário na tabela e armazena em uma lista @dadosColetados em seguida, em seguida verifica se o código está certo.
     * Se sim salva os dados no banco de dados -> @registrarUsusario.
     *
     * @param codigo contém o código da requisição do usuário.
     */
    @Override
    public TokenResponseDto autenticarUsuario(int codigo) {

        LOGGER.info("Início do método para autenticar o usuário - Service.");

        List<CodigoEmailDto> registros = iVerificarCodigoRepository.autenticarUsuario();

        List<UsuarioRequestDto> dadosColetados = iVerificarCodigoRepository.extrairDadosUsuario(emailDigitado, codigo);

        for (CodigoEmailDto registro : registros) {
            if (registro.getCodigo() == codigo && registro.getEmail().equals(emailDigitado)) {
                for (UsuarioRequestDto dadosUsuario : dadosColetados) {
                    if (registro.getCodigo() == codigo && dadosUsuario.getEmail().equals(emailDigitado)) {
                        iUsuarioCommand.registrarUsuario(dadosUsuario);

                        long id = iUsuarioRepositorio.buscarIdPorEmail(emailDigitado);

                        String token = jwtUtils.gerarToken(emailDigitado, id);

                        return new TokenResponseDto(token);
                    }
                }
            }
        }
        throw new CustomException(MensagensUtils.CODIGO_INVALIDO_OU_INCORRETO);
    }
}