package br.com.balaopreto.port.output;

import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
import br.com.balaopreto.adapter.input.dto.verificarCodigo.CodigoEmailDto;

import java.util.List;

public interface IVerificarCodigoRepository {
    void salvarCodigoVerificacao(UsuarioRequestDto request, int codigo);
    List<CodigoEmailDto> autenticarUsuario();
    List<UsuarioRequestDto> extrairDadosUsuario(String email, int codigo);
}
