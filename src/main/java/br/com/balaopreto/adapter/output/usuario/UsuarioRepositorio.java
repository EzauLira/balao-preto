package br.com.balaopreto.adapter.output.usuario;

import br.com.balaopreto.domain.entity.Usuario;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.output.IUsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositorio implements IUsuarioRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioRepositorio.class);
    private JdbcTemplate jdbcTemplate;

    public UsuarioRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Registra o usuário no banco de dados após verificar se os dados estão todos corretos anteriormente.
     *
     * @param usuario
     */
    public void registrarUsuario(Usuario usuario) {
        LOGGER.info("Início do método para registrar um usuário no banco de dados - repositório");


        LOGGER.info("Início do Try-Catch e preparamento do objeto para subir no Banco de dados");
        try {
            var sql = "INSERT INTO public.usuarios (nome, telefone, email) VALUES (?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) preparedStatment -> {
                preparedStatment.setString(1, usuario.getNome());
                preparedStatment.setString(2, usuario.getTelefone());
                preparedStatment.setString(3, usuario.getEmail());

                preparedStatment.execute();
                return null;
            });
        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao cadastrar usuário no banco de dados.");
        }
    }

    /**
     * Verifica se existe um usuário cadastrado com o e-mail informado.
     *
     * @param email O e-mail a ser verificado no banco de dados.
     * @return      true se o e-mail existir no sistema, false caso contrário.
     */
    @Override
    public boolean consultaUsuarior(String email) {
        LOGGER.info("Início do método para consultar o e-mail no banco de dados - repositório");

        try {
            var sql = "SELECT EXISTS (SELECT 1 FROM usuarios WHERE email = ?)";
            return jdbcTemplate.queryForObject(sql, Boolean.class, email);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar e-mail no banco de dados.");
        }
    }

    /**
     * Busca o ID do usuário associado ao e-mail informado.
     *
     * @param email O e-mail do usuário cujo ID será buscado.
     * @return      O ID do usuário encontrado no banco de dados.
     */
    @Override
    public int buscarIdPorEmail(String email) {
        LOGGER.info("Início do método para extrair o e-mail do usuário no banco de dados - Repositorio");

        try {
            var sql = "SELECT id FROM usuarios WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, email);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao extrair o email do usuário.");
        }
    }

    /**
     * Busca o ID do usuário associado ao número de telefone informado.
     *
     * @param telefone O número de telefone do usuário.
     * @return         O ID do usuário encontrado.
     */
    @Override
    public int buscarIdPorTelefone(String telefone) {
        LOGGER.info("Início do método para extrair o telefone do usuário no banco de dados - Repositório");

        try {

            String sql = "SELECT id FROM usuarios WHERE telefone = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, telefone);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao extrair o telefone do usuário.");
        }
    }

    /**
     * Verifica se o número de telefone já está cadastrado no sistema.
     *
     * @param telefone O número de telefone a ser verificado.
     * @return         true se o telefone existir, false caso contrário.
     */
    @Override
    public boolean existeTelefone(String telefone) {
        LOGGER.info("Início do método para verificar se o telefone existe no banco de dados - Repositório");

        try {

            String sql = "SELECT EXISTS (SELECT 1 FROM usuarios WHERE telefone = ?)";
            return jdbcTemplate.queryForObject(sql, Boolean.class, telefone);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao verificar existência do telefone.");
        }
    }
}