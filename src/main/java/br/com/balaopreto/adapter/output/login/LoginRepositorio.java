package br.com.balaopreto.adapter.output.login;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
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
    public void salvarCodigoAutenticacao(UsuarioRequestDto request, int codigo) {
        LOGGER.info("Início do método para salvar o código no banco de dados - Repositorio");

        try {
            var sql = "INSERT INTO verificacoes (tipo, email, codigo) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, TipoEnum.LOGIN.getTipo(), request.getEmail(), codigo);

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
    public List<Integer> autenticarUsuario() {
        LOGGER.info("Início do método para verificar código do usuário - Repositorio");

        try {
            var sql = "SELECT codigo FROM verificacoes WHERE tipo = 'Login'";
            return jdbcTemplate.queryForList(sql, Integer.class);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar código no banco de dados.");
        }
    }

    /**
     * Extrai os dados do usuáio da tabela de verificação e retorna para o chamador.
     * @return
     */
    @Override
    public List<UsuarioRequestDto> extrairEmail() {
        LOGGER.info("Início do método para extrair os dados do usuário no banco de dados - Repositorio");

        try {
            var sql = "SELECT email FROM usuarios";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new UsuarioRequestDto(
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("email")
            ));

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar código no banco de dados.");
        }
    }
}
