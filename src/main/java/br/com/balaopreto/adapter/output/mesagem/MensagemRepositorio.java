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

import java.util.ArrayList;
import java.util.List;

@Repository
public class MensagemRepositorio implements IMensagemRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(MensagemRepositorio.class);

    private final JdbcTemplate jdbcTemplate;

    public MensagemRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Salva uma nova mensagem no banco de dados.
     *
     * @param request A entidade Mensagem contendo os dados da mensagem a ser salva.
     */
    @Override
    public void salvarMensagem(Mensagem request) {
        LOGGER.info("Início do método salvarMensagem - Repositório");

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


    /**
     * Lista todas as mensagens trocadas entre dois usuários.
     *
     * @param usuarioId O ID do usuário logado.
     * @param contatoId O ID do contato com quem as mensagens foram trocadas.
     * @return Uma lista de mensagens entre os dois usuários.
     */
    @Override
    public List<Mensagem> listarMensagensEntreUsuarios(int usuarioId, int contatoId) {
        LOGGER.info("Início do método listarMensagensEntreUsuarios - Repositório");

        try {

            String sql = """
                        SELECT * FROM mensagens
                        WHERE (
                            (de_id = ? AND para_id = ? AND visivel_para_remetente = true)
                            OR
                            (de_id = ? AND para_id = ? AND visivel_para_destinatario = true)
                        )
                        ORDER BY data_hora ASC
                    """;

            return jdbcTemplate.query(sql,
                    new Object[]{usuarioId, contatoId, contatoId, usuarioId},
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


    /**
     * Lista as conversas mais recentes do usuário com seus contatos.
     *
     * @param usuarioId O ID do usuário logado.
     * @return Uma lista de DTOs com informações resumidas das últimas conversas.
     */
    @Override
    public List<ConversaResumoResponseDto> listarConversasRecentes(int usuarioId) {
        LOGGER.info("Início do método listarConversasRecentes - Repositório");

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


    /**
     * Busca mensagens não lidas de um usuário para outro.
     *
     * @param deId   O ID do remetente da mensagem.
     * @param paraId O ID do destinatário da mensagem.
     * @return Uma lista de mensagens não lidas.
     */
    @Override
    public List<Mensagem> buscarMensagensNaoLidas(int deId, int paraId) {
        LOGGER.info("Início do método buscarMensagensNaoLidas - Repositório");
        try {
            var sql = "SELECT * from mensagens WHERE de_id = ? AND para_id = ? AND status = 'ENVIADA' ORDER BY data_hora ASC";

            return jdbcTemplate.query(sql, new Object[]{deId, paraId}, (rs, rowNum) -> Mensagem.builder()
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

    /**
     * Marca todas as mensagens recebidas de um contato como lidas.
     *
     * @param usuarioId O ID do usuário que está marcando as mensagens como lidas.
     * @param contatoId O ID do contato cujas mensagens serão marcadas como lidas.
     */
    @Override
    public void marcarMensagensComoLidas(int usuarioId, int contatoId) {
        LOGGER.info("Início do método marcarMensagensComoLidas - Repositório");

        try {
            var sql = "UPDATE mensagens SET status = 'LIDA' WHERE para_id = ? AND de_id = ? AND status != 'LIDA' ";
            jdbcTemplate.update(sql, usuarioId, contatoId);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao salvar os dados do usuário no banco de dados.");
        }
    }

    /**
     * Lista a última mensagem trocada com cada contato para exibição de conversas recentes.
     *
     * @param usuarioId O ID do usuário logado.
     * @return Uma lista de mensagens representando a última troca com cada contato.
     */
    @Override
    public List<Mensagem> listarUltimaMensagemPorConversa(int usuarioId) {
        LOGGER.info("Início do método listarUltimaMensagemPorConversa - Repositório");

        try {
            var sql = """
                    SELECT DISTINCT ON (
                        LEAST(de_id, para_id), GREATEST(de_id, para_id)
                    ) *
                    FROM mensagens
                    WHERE de_id = ? OR para_id = ?
                    ORDER BY LEAST(de_id, para_id), GREATEST(de_id, para_id), data_hora DESC
                    """;
            return jdbcTemplate.query(sql, new Object[]{usuarioId, usuarioId}, (rs, rowNuw) ->
                    Mensagem.builder()
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

    /**
     * Busca uma mensagem pelo seu identificador único.
     *
     * @param id O ID da mensagem a ser buscada.
     * @return A mensagem encontrada ou null se não existir.
     */
    @Override
    public Mensagem buscarMensagemPorId(int id) {
        LOGGER.info("Início do método buscarMensagemPorId - Repositório");

        String sql = "SELECT * FROM mensagens WHERE id = ?";
        List<Mensagem> mensagens = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> Mensagem.builder()
                .id(rs.getInt("id"))
                .deId(rs.getInt("de_id"))
                .paraId(rs.getInt("para_id"))
                .conteudo(rs.getString("conteudo"))
                .status(rs.getString("status"))
                .datahora(rs.getString("data_hora"))
                .visivelParaRemetente(rs.getBoolean("visivel_para_remetente"))
                .visivelParaDestinatario(rs.getBoolean("visivel_para_destinatario"))
                .build());

        return mensagens.isEmpty() ? null : mensagens.get(0);
    }

    /**
     * Atualiza a visibilidade de uma mensagem para o remetente ou destinatário.
     *
     * @param mensagemId          O ID da mensagem.
     * @param visivelRemetente    Se verdadeiro, define a mensagem como visível para o remetente.
     * @param visivelDestinatario Se verdadeiro, define a mensagem como visível para o destinatário.
     */
    @Override
    public void atualizarVisibilidadeMensagem(int mensagemId, Boolean visivelRemetente, Boolean visivelDestinatario) {
        LOGGER.info("Início do método atualizarVisibilidadeMensagem - Repositório");

        StringBuilder sql = new StringBuilder("UPDATE mensagens SET ");
        List<Object> params = new ArrayList<>();

        if (visivelRemetente != null) {
            sql.append("visivel_para_remetente = ?, ");
            params.add(visivelRemetente);
        }

        if (visivelDestinatario != null) {
            sql.append("visivel_para_destinatario = ?, ");
            params.add(visivelDestinatario);
        }

        // remove vírgula final
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        params.add(mensagemId);

        jdbcTemplate.update(sql.toString(), params.toArray());
    }

}