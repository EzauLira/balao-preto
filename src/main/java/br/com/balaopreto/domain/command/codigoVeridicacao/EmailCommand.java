package br.com.balaopreto.domain.command.codigoVeridicacao;

import br.com.balaopreto.domain.enuns.EmailEnum;
import br.com.balaopreto.domain.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailCommand {

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
    @Async
    public void enviarEmail(String email, int codigo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
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
        String modelo = EmailEnum.CADIGO_VERIFICACAO.getModelo();
        return modelo
                .replace("{codigo}", codigo);
    }
}