package br.com.balaopreto.adapter.input.usuario;

import br.com.balaopreto.domain.command.usuario.UsuarioCommand;
import br.com.balaopreto.port.input.IUsuarioCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/usuario")
public class UsuarioController implements IUsuarioController {

    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

    private IUsuarioCommand iUsuarioCommand;
    public UsuarioController(UsuarioCommand iUsuarioCommand){
        this.iUsuarioCommand = iUsuarioCommand;
    }

}
