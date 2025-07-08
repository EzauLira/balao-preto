package br.com.balaopreto.adapter.input.contato;

import br.com.balaopreto.adapter.input.dto.contato.ContatoRequestDto;
import br.com.balaopreto.adapter.output.seguranca.JwtUtils;
import br.com.balaopreto.config.dto.StandardResponseDto;
import br.com.balaopreto.port.input.IContatoCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/contatos")
public class ContatoController implements IContatoController {
    private final JwtUtils jwtUtils;
    private final IContatoCommand iContatoCommand;
    public ContatoController(JwtUtils jwtUtils, IContatoCommand iContatoCommand){
        this.jwtUtils = jwtUtils;
        this.iContatoCommand = iContatoCommand;
    }

    @Override
    @PostMapping("/adcionar")
    public ResponseEntity<StandardResponseDto> adicionarContato(@RequestBody ContatoRequestDto request, @RequestHeader("Autorization") String token){

        int idUsusarioLogado = Integer.parseInt(jwtUtils.extrairID(token));
        iContatoCommand.adicionarContato(idUsusarioLogado, request);

        return ResponseEntity.ok(StandardResponseDto.builder().message("Contato adicionado com sucesso.").build());
    }

    @Override
    @GetMapping("/contatos")
    public ResponseEntity<?> listarContatos(@RequestHeader("Authorization") String token){
        int idUsusarioLogado = Integer.parseInt(jwtUtils.extrairID(token));
        return ResponseEntity.status(HttpStatus.OK).body(idUsusarioLogado);
    }

}
