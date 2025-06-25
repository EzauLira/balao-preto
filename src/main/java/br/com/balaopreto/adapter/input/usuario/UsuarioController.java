//package br.com.balaopreto.adapter.input.usuario;
//
//import br.com.balaopreto.adapter.input.dto.usuario.UsuarioRequestDto;
//import br.com.balaopreto.config.dto.StandardResponseDto;
//import br.com.balaopreto.domain.command.usuario.UsuarioCommand;
//import br.com.balaopreto.port.input.IUsuarioCommand;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController("/v1/usuario")
//public class UsuarioController implements IUsuarioController {
//
//    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
//
//    private IUsuarioCommand iUsuarioCommand;
//    public UsuarioController(UsuarioCommand iUsuarioCommand){
//        this.iUsuarioCommand = iUsuarioCommand;
//    }
//    @Override
//    @PostMapping("/registrar")
//    public ResponseEntity<StandardResponseDto> registrarUsuario(@RequestBody UsuarioRequestDto usuarioRequestDto) {
//
//        LOGGER.info("Início do método para cadastrar um novo usuário - controller");
//
//        iUsuarioCommand.registrarUsuario(usuarioRequestDto);
//
//        return ResponseEntity.ok(StandardResponseDto.builder().message("Usuário Registrado com sucesso!").build());
//    }
//}
