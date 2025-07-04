package br.com.balaopreto.domain.command.RecuperarConta;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IEmailCommand;
import br.com.balaopreto.port.input.IRecuperarContaCommand;
import br.com.balaopreto.port.output.IRecuperarContaRepositorio;
import br.com.balaopreto.utils.CodigoUtils;
import br.com.balaopreto.utils.constantes.MensagensUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecuperarContaCommand implements IRecuperarContaCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecuperarContaCommand.class);

    private final IRecuperarContaRepositorio iRecuperarContaRepositorio;
    private final IEmailCommand iEmailCommand;
    private String emailColetado;

    public RecuperarContaCommand(IRecuperarContaRepositorio iRecuperarContaRepositorio, IEmailCommand iEmailCommand) {
        this.iRecuperarContaRepositorio = iRecuperarContaRepositorio;
        this.iEmailCommand = iEmailCommand;
    }

    /**
     * Esse método recebe o Telefone do usuário, verifica se já existe no banco dados e extrai o e-mail: @extrairEmail
     * Salvo em uma lista esse e-mail, em seguida verifico se a cunsulta é vazia se não for eu gero um código de 4 digitos
     * pego o indice do e-mail salvo no @emailColetado, mando um e-mail para o e-mail do usuário e salvo também no banco de dados temparário.
     * @param telefone contém os dados da requisição do usuário.
     */
    @Override
    public String recuperarConta(String telefone) {
        LOGGER.info("Início do método para recuperar conta do usuário - Service.");

        List<String> consultarEmail = iRecuperarContaRepositorio.extrairEmail(telefone);

        if (!consultarEmail.isEmpty()){

            var codigo = CodigoUtils.gerarCodigo4Digitos();
            emailColetado = consultarEmail.get(0);

            LOGGER.info("Entrando no método Repositório - Service ");
            iRecuperarContaRepositorio.salvarCodigoAutenticacao(emailColetado, codigo);


            LOGGER.info("Entrando no método para enviar e-mail para usuário - Service ");
            iEmailCommand.enviarEmailRecuperacao(emailColetado, codigo);
            return emailColetado;

        }else {
            throw new BaseException(MensagensUtils.ERRO_TELEFONE);
        }
    }

    /**
     * Esse método tem como princípio validar o usuário/recuperar conta.
     * Ele pega o código que o usuário recebeu no e-mail em seguida verifica se no banco tem o mesmo código @autentiarConta.
     * Ele pega os dados do usuário na tabela e armazena em uma lista @codigoBanco, em seguida verifica se o código está certo e retorna.
     * @param codigo contém o código da requisição do usuário.
     */
    @Override
    public void autenticarConta(int codigo) {

        LOGGER.info("Início do método para verificar se o código existe - Service.");

        List<CodigoEmailDto> registros = iRecuperarContaRepositorio.autenticarConta();

        for (CodigoEmailDto registro : registros) {
            if (registro.getCodigo() == codigo && registro.getEmail().equals(emailColetado)) {
                return;
            }
        }

        throw new CustomException(MensagensUtils.CODIGO_INVALIDO_OU_INCORRETO);
    }
}