package br.com.balaopreto.domain.enuns;

public enum EmailEnum {

    CADIGO_VERIFICACAO(
            "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "<title>Código de Verificalção</title>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; margin: 0; padding: 20px; }" +
                    ".container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    "h1 { color: #ee8c0e; }" +
                    "p { line-height: 1.6; }" +
                    ".event-info { margin-top: 20px; }" +
                    ".event-info p { margin: 5px 0; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h1>Código de Verificação.</h1>" +
                    "<p>Olá!</p>" +
                    "<p>Seu código de verificação:  <strong>{codigo}</strong></p>" +
                    "<div class='event-info'>" +
                    "</div>" +
                    "<p>Agradecemos por se inscrever!</p>" +
                    "<p>Atenciosamente,</p>" +
                    "<p><em>Equipe de Organização</em></p>" +
                    "</div>" +
                    "</body>" +
                    "</html>"
    );
    private final String modelo;

    EmailEnum(String modelo)
    {
        this.modelo = modelo;
    }

    public String getModelo()
    {
        return modelo;
    }

}
