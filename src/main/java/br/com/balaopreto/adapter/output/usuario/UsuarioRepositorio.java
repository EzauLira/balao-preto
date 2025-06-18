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

    public void registrarUsuario(Usuario usuario) {
        LOGGER.info("Início do método para registrar um usuário no banco de dados - Patrimony");


        LOGGER.info("Início do Try-Catch e preparamento do objeto para subir no Banco de dados");
        try {
            var sql = "SELECT * FROM registrar_usuario(?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) preparedStatment -> {
                preparedStatment.setString(1, usuario.getNome());
                preparedStatment.setString(2, usuario.getEmail());
                preparedStatment.setString(3, usuario.getSenha());
                preparedStatment.setDouble(4, usuario.getTelefone());
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
}