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

import java.util.List;

@Repository
public class UsuarioRepositorio implements IUsuarioRepositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioRepositorio.class);
    private JdbcTemplate jdbcTemplate;

    public UsuarioRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Registra o usuário no banco de dados após verificar se os dados estão todos corretos anteriormente.
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

    @Override
    public List<String> consultaUsuarior() {
        LOGGER.info("Início do método para consultar o e-mail no banco de dados - repositório");

        try {
            var sql = "SELECT email From usuarios";
            return jdbcTemplate.queryForList(sql, String.class);

        } catch (DataAccessException e) {
            LOGGER.error("DataAccessException: {}", e.getMessage(), e);
            throw new BaseException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage(), e);
            throw new CustomException("Erro ao buscar e-mail no banco de dados.");
        }
    }
}