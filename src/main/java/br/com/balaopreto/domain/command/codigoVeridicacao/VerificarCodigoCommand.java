package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.adapter.output.verificarCodigo.VerificacaoCodigoRepository;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.utils.CodigoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificarCodigoCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificarCodigoCommand.class);

    private final VerificacaoCodigoRepository verificacaoCodigoRepository;

    private final EmailCommand emailCommand;

    public VerificarCodigoCommand(VerificacaoCodigoRepository verificacaoCodigoRepository, EmailCommand emailCommand) {
        this.verificacaoCodigoRepository = verificacaoCodigoRepository;
        this.emailCommand = emailCommand;
    }

    public void salvarCodigoVerificacao(String email) {

        LOGGER.info("Início do método para salvar código no banco de dados - Service.");

        int codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service ");
        verificacaoCodigoRepository.salvarCodigoVerificacao(email, codigo);

        emailCommand.enviarEmail(email, codigo);
    }

    public void autenticarUsuario(int codigo) {

        LOGGER.info("Início do método para verificar se o código existe - Service.");

        List<Integer> codigoBanco = verificacaoCodigoRepository.autenticarUsuario();

        for (int lista : codigoBanco) {
            if (codigo == lista)
                return;

        }
        throw new CustomException("Código não existe ou esta inválido.");

    }
}
