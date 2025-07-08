package br.com.balaopreto.adapter.output.verificarCodigo;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;
import br.com.balaopreto.domain.enuns.TipoEnum;
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
public class VerificarCodigoRepositorio implements IVerificarCodigoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificarCodigoRepositorio.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Insere os dados já validados anteriormente na tabela do banco de dados.
     * @param request requisição do usuário.
     * @param codigo código enviado para o banco.
     */
    @Override
    public void salvarCodigoVerificacao(UsuarioRequestDto request, int codigo) {
        LOGGER.info("Início do método para salvar o código no banco de dados - Repositorio");

        try {
            var sql = "INSERT INTO verificacoes (tipo, nome, telefone, email, codigo) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, TipoEnum.CADASTRO.getTipo(), request.getNome(), request.getTelefone(), request.getEmail(), codigo);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar o código no banco de dados.");
        }
    }

    /**
     * Confirma o código inserido na requisição pelo usuário.
     * @return
     */
    @Override
    public List<CodigoEmailDto> autenticarUsuario() {
        LOGGER.info("Início do método autenticar o usuário no anco de dados - Repositorio");

        try {
            var sql = "SELECT codigo, email FROM verificacoes WHERE tipo = 'Cadastro'";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new CodigoEmailDto(
                    rs.getInt("codigo"),
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

    /**
     * Extrai os dados do usuáio da tabela de verificação e retorna para o chamador.
     * @return
     */
    @Override
    public List<UsuarioRequestDto> extrairDadosUsuario(String email, int codigo) {
        LOGGER.info("Início do método para extrair os dados do usuário no banco de dados - Repositorio");

        try {
            var sql = "SELECT nome, telefone, email FROM verificacoes WHERE tipo = 'Cadastro' AND email = ? AND codigo = ?";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new UsuarioRequestDto(
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("email")), email, codigo);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao extrair os dado do usuário no banco.");
        }
    }

    /**
     * Deleta o conteúdo da tabela verificações a cada 5 min.
     */
    @Scheduled(fixedRate = 60000)
    public void limparVerificacoesExpiradas() {
        var sql = "DELETE FROM verificacoes WHERE criado_em < NOW() - INTERVAL '5 minutes'";
        jdbcTemplate.update(sql);
    }
}
