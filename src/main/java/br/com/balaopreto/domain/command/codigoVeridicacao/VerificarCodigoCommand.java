package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IEmailCommand;
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

    private final IEmailCommand iEmailCommand;

    public VerificarCodigoCommand(IVerificarCodigoRepository iVerificarCodigoRepository, IEmailCommand iEmailCommand) {
        this.iVerificarCodigoRepository = iVerificarCodigoRepository;
        this.iEmailCommand = iEmailCommand;
    }

    @Override
    public void salvarCodigoVerificacao(String email) {

        LOGGER.info("Início do método para salvar código no banco de dados - Service.");

        var codigo = CodigoUtils.gerarCodigo4Digitos();

        LOGGER.info("Entrando no método Repositório - Service ");
        iVerificarCodigoRepository.salvarCodigoVerificacao(email, codigo);

        iEmailCommand.enviarEmail(email, codigo);
    }

    @Override
    public void autenticarUsuario(int codigo) {

        LOGGER.info("Início do método para verificar se o código existe - Service.");

        List<Integer> codigoBanco = iVerificarCodigoRepository.autenticarUsuario();

        for (int lista : codigoBanco) {
            if (codigo == lista)
                return;

        }
        throw new CustomException("Código não existe ou esta inválido.");
    }
}