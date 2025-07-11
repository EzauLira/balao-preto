package br.com.balaopreto.adapter.output.mesagem;

import br.com.balaopreto.adapter.input.dto.mensagem.ConversaResumoResponseDto;
import br.com.balaopreto.domain.entity.Mensagem;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.IMensagemRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MensagemRepositorio implements IMensagemRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(MensagemRepositorio.class);

    private final JdbcTemplate jdbcTemplate;

    public MensagemRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void salvarMensagem(Mensagem request) {

        try {
            String sql = "INSERT INTO mensagens (de_id, para_id, conteudo, status) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, request.getDeId(), request.getParaId(), request.getConteudo(), request.getStatus());

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar os dados do usuário no banco de dados.");
        }
    }

    @Override
    public List<Mensagem> listarMensagensEntreUsuarios(int usuarioId, int contatoId) {

        try {

            String sql = "SELECT * FROM mensagens " +
                    "WHERE (de_id = ? AND para_id = ?) OR (de_id = ? AND para_id = ?) " +
                    "ORDER BY data_hora ASC";

            return jdbcTemplate.query(sql, new Object[]{usuarioId, contatoId, contatoId, usuarioId},
                    (rs, rowNum) -> Mensagem.builder()
                            .id(rs.getInt("id"))
                            .deId(rs.getInt("de_id"))
                            .paraId(rs.getInt("para_id"))
                            .conteudo(rs.getString("conteudo"))
                            .datahora(rs.getString("data_hora"))
                            .status(rs.getString("status"))
                            .build());

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar os dados do usuário no banco de dados.");
        }
    }

    @Override
    public List<ConversaResumoResponseDto> listarConversasRecentes(int usuarioId) {

        try {
            var sql = """
                    SELECT 
                        c.apelido AS nome_contato,
                        u.telefone AS telefone_contato,
                        m.conteudo AS ultima_mensagem,
                        m.data_hora,
                        CASE WHEN m.de_id = ? THEN true ELSE false END AS enviada_por_mim
                    FROM mensagens m
                    JOIN usuarios u ON 
                        CASE 
                            WHEN m.de_id = ? THEN m.para_id = u.id
                            ELSE m.de_id = u.id
                        END
                    JOIN contatos c ON (
                        (m.de_id = ? AND c.usuario_id = m.de_id AND c.contato_id = m.para_id)
                        OR 
                        (m.para_id = ? AND c.usuario_id = m.para_id AND c.contato_id = m.de_id)
                    )
                    WHERE (m.de_id = ? OR m.para_id = ?)
                      AND m.data_hora = (
                          SELECT MAX(data_hora)
                          FROM mensagens
                          WHERE (de_id = m.de_id AND para_id = m.para_id)
                             OR (de_id = m.para_id AND para_id = m.de_id)
                      )
                    ORDER BY m.data_hora DESC
                    """;

            return jdbcTemplate.query(sql, new Object[]{usuarioId, usuarioId, usuarioId, usuarioId, usuarioId, usuarioId}, (rs, rowNum) ->
                    ConversaResumoResponseDto.builder()
                            .nomeContato(rs.getString("nome_contato"))
                            .telefoneContato(rs.getString("telefone_contato"))
                            .ultimaMensagem(rs.getString("ultima_mensagem"))
                            .dataHora(rs.getString("data_hora"))
                            .mensagemMinha(rs.getBoolean("enviada_por_mim"))
                            .build());
        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar os dados do usuário no banco de dados.");
        }
    }
}