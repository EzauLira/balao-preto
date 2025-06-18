package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.domain.enuns.EmailEnum;
import br.com.balaopreto.domain.exception.CustomException;
import br.com.balaopreto.port.input.IEmailCommand;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailCommand implements IEmailCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailCommand.class);

    private JavaMailSender emailSender;

    public EmailCommand(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Envia um e-mail de confirmação de inscrição.
     *
     * @param email O endereço de e-mail do destinatário.
     *
     */
    @Override
    @Async
    public void enviarEmail(String email, int codigo) {
        LOGGER.info("Início do método para envio do o email - controller");
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setText(gerarCorpoEmail(String.valueOf(codigo)), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException("Erro ao enviar e-mail");
        }
    }

    /**
     * Gera o corpo do e-mail de confirmação de inscrição.
     *
     * @param codigo  O codigo de verificação.
     * @return O corpo do e-mail em formato HTML.
     */
    private String gerarCorpoEmail(String codigo) {
        LOGGER.info("Início do método para gerar o corpo d o email - controller");
        var modelo = EmailEnum.CADIGO_VERIFICACAO.getModelo();
        return modelo
                .replace("{codigo}", codigo);
    }
}