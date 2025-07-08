package br.com.balaopreto.adapter.input.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.config.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IContatoController {


    ResponseEntity<StandardResponseDto> adicionarContato(@RequestBody ContatoRequestDto request, @RequestHeader("Autorization") String token);

    ResponseEntity<?> listarContatos(@RequestHeader("Authorization") String token);
}
