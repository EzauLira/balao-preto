package br.com.balaopreto.domain.command.login;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
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

    private ILoginRepositorio iLoginRepositorio;
    private IEmailCommand iEmailCommand;
    private IUsuarioRepositorio iUsuarioRepositorio;

    public LoginCommand(ILoginRepositorio iLoginRepositorio, IEmailCommand iEmailCommand, IUsuarioRepositorio iUsuarioRepositorio) {
        this.iUsuarioRepositorio = iUsuarioRepositorio;
        this.iLoginRepositorio = iLoginRepositorio;
        this.iEmailCommand = iEmailCommand;

    }

    /**
     * Esse método recebe os dados do usuário, verifica se já existe no banco dados @consultaUsuarior, se sim não prossegue, se não ele gera
     * um código aleatório de quatro dígitos, esse código é enviado para o banco de dados pelo método: @salvarCodigoVerificacao.
     * Consulta a tabela de usuários para verificar se o e-mail passado já existe no banco de dados.
     * Em seguida envia para o e-mail do usuário o mesmo código através do método @enviarEmail.
     * @param request contém os dados da requisição do usuário.
     */
    @Override
    public void logar(UsuarioRequestDto request){
        LOGGER.info("Início do método para efetuar login do usuário - Service.");

        var codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service.");
        iLoginRepositorio.salvarCodigoAutenticacao(request, codigo);

        iEmailCommand.enviarEmailAutenticacao(request.getEmail(), codigo);
    }

    /**
     * Esse método tem como princípio validar o usuário/confirmar conta.
     * Ele pega o código que o usuário recebeu no e-mail em seguida verifica se no banco tem o mesmo código @autentiarUsuario.
     * Ele pega os dados do usuário na tabela e armazena em uma lista @dadosColetados em seguida, em seguida verifica se o código está certo.
     * Se sim salva os dados no banco de dados -> @registrarUsusario.
     * @param codigo contém o código da requisição do usuário.
     */
    @Override
    public void autenticarUsuario(int codigo) {
        LOGGER.info("Início do método para verificar se o código existe - Service.");

        List<Integer> codigoBanco = iLoginRepositorio.autenticarUsuario();
        List<UsuarioRequestDto> emailUsuario = iLoginRepositorio.extrairEmail();

        for (int lista : codigoBanco) {
            if (codigo == lista) {
                for (UsuarioRequestDto email : emailUsuario) {
                    if (email.equals(emailUsuario))
                        throw new CustomException("Usuário já possui cadastro.");
                }
                return;
            }
        }
        throw new CustomException(MensagensUtils.CODIGO_INVALIDO_OU_INCORRETO);
    }
}
