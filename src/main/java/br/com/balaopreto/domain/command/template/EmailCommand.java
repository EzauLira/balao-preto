package br.com.balaopreto.domain.command.template;

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
    private final TemplateCommand templateCommand;

    public EmailCommand(JavaMailSender emailSender, TemplateCommand templateCommand) {
        this.emailSender = emailSender;
        this.templateCommand = templateCommand;
    }

    /**
     * Envia um e-mail de confirmação de inscrição.
     *
     * @param email O endereço de e-mail do destinatário.
     *
     */
    @Override
    @Async
    public void enviarEmailVerificacao(String email, int codigo) {
        LOGGER.info("Início do método para envio do e-mail - EmailCommand");

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Código de Verificação");
            helper.setText(gerarCorpoEmailVerificacao(String.valueOf(codigo)), true);

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
    private String gerarCorpoEmailVerificacao(String codigo) {
        try {
            String template = templateCommand.carregarTemplate("verificacao");
            return templateCommand.substituirVariaveis(template, "codigo", codigo);
        } catch (Exception e) {
            throw new CustomException("Erro ao carregar template de e-mail");
        }
    }

    /**
     * Envia um e-mail de confirmação de inscrição.
     *
     * @param email O endereço de e-mail do destinatário.
     */
    @Override
    @Async
    public void enviarEmailAutenticacao(String email, int codigo) {
        LOGGER.info("Início do método para envio do e-mail - EmailCommand");

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Código de Autenticação");
            helper.setText(gerarCorpoEmailAutenticacao(String.valueOf(codigo)), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException("Erro ao enviar e-mail");
        }
    }

    /**
     * Gera o corpo do e-mail de confirmação.
     *
     * @param codigo  O codigo de verificação.
     * @return O corpo do e-mail em formato HTML.
     */
    private String gerarCorpoEmailAutenticacao(String codigo) {
        try {
            String template = templateCommand.carregarTemplate("autenticacao");
            return templateCommand.substituirVariaveis(template, "codigo", codigo);
        } catch (Exception e) {
            throw new CustomException("Erro ao carregar template de e-mail");
        }
    }

    /**
     * Envia um e-mail de confirmação de inscrição.
     * @param email O endereço de e-mail do destinatário.
     */
    @Override
    @Async
    public void enviarEmailRecuperacao(String email, int codigo) {
        LOGGER.info("Início do método para envio do e-mail - EmailCommand");

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Código de Autenticação");
            helper.setText(gerarCorpoEmailRecuperacao(String.valueOf(codigo)), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException("Erro ao enviar e-mail");
        }
    }

    /**
     * Gera o corpo do e-mail de confirmação.
     * @param codigo  O codigo de verificação.
     * @return O corpo do e-mail em formato HTML.
     */
    private String gerarCorpoEmailRecuperacao(String codigo) {
        try {
            String template = templateCommand.carregarTemplate("autenticacao");
            return templateCommand.substituirVariaveis(template, "codigo", codigo);
        } catch (Exception e) {
            throw new CustomException("Erro ao carregar template de e-mail");
        }
    }
}