package br.com.balaopreto.domain.command.login;

import br.com.balaopreto.adapter.input.dto.jwt.TokenResponseDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IEmailCommand;
import br.com.balaopreto.port.input.ILoginCommand;
import br.com.balaopreto.port.output.ILoginRepositorio;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import br.com.balaopreto.utils.CodigoUtils;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginCommand implements ILoginCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(LoginCommand.class);

    private final ILoginRepositorio iLoginRepositorio;
    private final IEmailCommand iEmailCommand;
    private String emailDigitado;
    private final JwtUtils jwtUtils;
    private final IUsuarioRepositorio iUsuarioRepositorio;

    public LoginCommand(ILoginRepositorio iLoginRepositorio, IEmailCommand iEmailCommand, JwtUtils jwtUtils, IUsuarioRepositorio iUsuarioRepositorio) {
        this.iLoginRepositorio = iLoginRepositorio;
        this.iEmailCommand = iEmailCommand;
        this.jwtUtils = jwtUtils;
        this.iUsuarioRepositorio = iUsuarioRepositorio;
    }

    /**
     * Esse método recebe os dados do usuário, verifica se já existe no banco dados @consultaUsuarior, se sim não prossegue, se não ele gera
     * um código aleatório de quatro dígitos, esse código é enviado para o banco de dados pelo método: @salvarCodigoVerificacao.
     * Consulta a tabela de usuários para verificar se o e-mail passado já existe no banco de dados.
     * Em seguida envia para o e-mail do usuário o mesmo código através do método @enviarEmail.
     *
     * @param email contém os dados da requisição do usuário.
     */
    @Override
    public void iniciarLogin(String email) {
        emailDigitado = email;
        LOGGER.info("Início do método para efetuar login do usuário - Service.");

        if (!iLoginRepositorio.existeEmail(email)) {
            throw new CustomException("Parece que esse e-mail não foi encontrado com cadastro.");
        }

        var codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service.");
        iLoginRepositorio.salvarCodigoAutenticacao(email, codigo);

        iEmailCommand.enviarEmailAutenticacao(email, codigo);
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
        LOGGER.info("Início do método para verificar se o código existe - Service.");
        boolean flag = false;

        List<CodigoEmailDto> registros = iLoginRepositorio.autenticarUsuario();

        for (CodigoEmailDto registro : registros) {
            if (registro.getCodigo() == codigo && registro.getEmail().equals(emailDigitado)) {
                flag = true;
            }
        }
        if (!flag)
            throw new CustomException(MensagensUtils.CODIGO_INVALIDO_OU_INCORRETO);

        Long id = iUsuarioRepositorio.buscarIdPorEmail(emailDigitado); // buscar o id real do usuário
        String token = jwtUtils.gerarToken(emailDigitado, id);

        return new TokenResponseDto(token);
    }
}