package br.com.balaopreto.adapter.output.verificarCodigo;

import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.IVerificarCodigoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VerificarCodigoRepository implements IVerificarCodigoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificarCodigoRepository.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void salvarCodigoVerificacao(String email, int codigo) {
        LOGGER.info("Início do método para salvar o código no banco de dados - Patrimony");

        try {
            var sql = "INSERT INTO verificacoes (tipo, destino, codigo) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, "email", email, codigo);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao cadastrar usuário no banco de dados.");
        }
    }

    @Override
    public List<Integer> autenticarUsuario() {
        LOGGER.info("Início do método para salvar o código no banco de dados - Patrimony");

        try {
            var sql = "SELECT codigo From verificacoes";
            return jdbcTemplate.queryForList(sql, Integer.class);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar código no banco de dados.");
        }
    }

    @Scheduled(fixedRate = 60000)
    public void limparVerificacoesExpiradas() {
        var sql = "DELETE FROM verificacoes WHERE criado_em < NOW() - INTERVAL '5 minutes'";
        jdbcTemplate.update(sql);
    }
}
