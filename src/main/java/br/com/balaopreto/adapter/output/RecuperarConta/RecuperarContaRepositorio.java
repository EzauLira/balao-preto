package br.com.balaopreto.adapter.output.RecuperarConta;

import br.com.balaopreto.domain.enuns.TipoEnum;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.IRecuperarContaRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecuperarContaRepositorio implements IRecuperarContaRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecuperarContaRepositorio.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Salva o código no banco de dados para ser validado em seguida.
     * @param email email do usuário.
     * @param codigo código de verificação.
     */
    @Override
    public void salvarCodigoAutenticacao(String email, int codigo) {
        LOGGER.info("Início do método para salvar o código no banco de dados - Repositorio");

        try {
            var sql = "INSERT INTO verificacoes (tipo, email, codigo) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, TipoEnum.RECUPERAR.getTipo(), email, codigo);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar op código e email no banco de dados..");
        }
    }

    /**
     * Extrai o e-mail do usuário da tabela de usuarios
     * e retorna para o chamador.
     * @return
     */
    @Override
    public List<String> extrairEmail(String telefone) {
        LOGGER.info("Início do método para extrair o e-mail do usuário no banco de dados");

        try {
            var sql = "SELECT email FROM usuarios WHERE telefone = ?";
            return jdbcTemplate.queryForList(sql, String.class, telefone);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar e-mail no banco de dados.");
        }
    }

    /**
     * Confirma o código inserido na requisição pelo usuário.
     * @return
     */
    @Override
    public List<Integer> autenticarConta() {
        LOGGER.info("Início do método para verificar código do usuário - Repositorio");

        try {
            var sql = "SELECT codigo FROM verificacoes WHERE tipo = 'Recuperar'";
            return jdbcTemplate.queryForList(sql, Integer.class);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao tentar autenticar a conta.");
        }
    }
}
