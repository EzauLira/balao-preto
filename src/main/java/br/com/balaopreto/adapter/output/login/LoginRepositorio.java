package br.com.balaopreto.adapter.output.login;

import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;
import br.com.balaopreto.domain.enuns.TipoEnum;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.ILoginRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginRepositorio implements ILoginRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRepositorio.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void salvarCodigoAutenticacao(String email, int codigo) {
        LOGGER.info("Início do método para salvar o código no banco de dados - Repositorio");

        try {
            var sql = "INSERT INTO verificacoes (tipo, email, codigo) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, TipoEnum.LOGIN.getTipo(), email, codigo);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar os dados do usuário no banco de dados.");
        }
    }

    /**
     * Confirma o código inserido na requisição pelo usuário.
     * @return
     */
    @Override
    public List<CodigoEmailDto> autenticarUsuario() {
        LOGGER.info("Início do método para autenticar o usuário - Repositorio");

        try {
            var sql = "SELECT codigo, email FROM verificacoes WHERE tipo = 'Login'";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new CodigoEmailDto (
                    rs.getInt("codigo"),
                    rs.getString("email")
            ));

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao tenrtar autenticar usuário.");
        }
    }

    /**
     * Extrai o e-mail do usuário da tabela de usuarios
     * e retorna para o chamador.
     * @return
     */
    @Override
    public boolean existeEmail(String email) {
        LOGGER.info("Início do método para extrair o e-mail do usuário no banco de dados - Repositorio");

        try {
            var sql = "SELECT EXISTS (SELECT 1 FROM usuarios WHERE email = ?)";
            return jdbcTemplate.queryForObject(sql, Boolean.class, email);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao extrair o emauil do usuário.");
        }
    }

    @Override
    public List<String> extrairEmailVerificacao() {
        LOGGER.info("Início do método para extrair o e-mail do usuário no banco de dados - Repositorio");

        try {

            var sql = "SELECT email FROM verificacoes WHERE tipo = 'Login'";
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("email"));

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao extrair o emauil do usuário.");
        }
    }
}