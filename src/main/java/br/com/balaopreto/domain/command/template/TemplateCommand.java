package br.com.balaopreto.domain.command.template;

import br.com.balaopreto.port.input.ITemplateCommand;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class TemplateCommand implements ITemplateCommand {

    public String carregarTemplate(String nomeTemplate) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/" + nomeTemplate + ".html");
        try (InputStream inputStream = resource.getInputStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    public String substituirVariaveis(String template, String chave, String valor) {
        return template.replace("{" + chave + "}", valor);
    }
}
