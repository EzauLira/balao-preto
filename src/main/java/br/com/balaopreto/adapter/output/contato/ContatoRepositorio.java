package br.com.balaopreto.adapter.output.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoResponseDto;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.IContatoRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContatoRepositorio implements IContatoRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContatoRepositorio.class);

    private final JdbcTemplate jdbcTemplate;

    public ContatoRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Adiciona um novo contato ao usuário no banco de dados.
     *
     * @param usuarioId  O ID do usuário que está adicionando o contato.
     * @param contatoId  O ID do contato a ser adicionado.
     * @param apelido    O apelido atribuído ao contato.
     */
    @Override
    public void adicionarContato(int usuarioId, int contatoId, String apelido) {
        LOGGER.info("Início do método para adicionar um contato ao usuário - Repositório");

        try {
            var sql = "INSERT INTO contatos (usuario_id, contato_id, apelido) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, usuarioId, contatoId, apelido);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao adicionar contato ao usuário.");
        }
    }


    /**
     * Verifica se o contato já foi adicionado anteriormente pelo usuário.
     *
     * @param usuarioId  O ID do usuário que deseja verificar o contato.
     * @param contatoId  O ID do contato a ser verificado.
     * @return           true se o contato já existir, false caso contrário.
     */
    @Override
    public boolean contatoJaExiste(int usuarioId, int contatoId) {
        LOGGER.info("Início do método para verificar se o contato já existe para o usuário - Repositório");

        try {
            String sql = "SELECT COUNT(*) FROM contatos WHERE usuario_id = ? AND contato_id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId, contatoId);
            return count != null && count > 0;

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao verificar se o contato já existe para o usuário.");
        }
    }

    @Override
    public List<ContatoResponseDto> listarContatos(int usuarioId) {
        String sql = "SELECT u.nome, u.telefone, c.apelido FROM contatos c INNER JOIN usuarios u ON c.contato_id = u.id WHERE c.usuario_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ContatoResponseDto(
                                rs.getString("nome"),
                                rs.getString("telefone"),
                                rs.getString("apelido")
                        ),
                usuarioId
        );
    }
}