package br.com.balaopreto.port.input;

public interface ITemplateCommand {

    String carregarTemplate(String nomeTemplate) throws Exception;
    String substituirVariaveis(String template, String chave, String valor);
}
