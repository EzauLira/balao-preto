package br.com.balaopreto.utils.validadores;

import br.com.balaopreto.utils.constantes.MensagemErroUtils;
import br.com.balaopreto.domain.exception.CustomException;

/**
 * Classe utilitária para validação de e-mails.
 */
public class ValidarEmailUtils {

    /**
     * Valida o e-mail fornecido.
     * Lança uma exceção se o e-mail for inválido.
     *
     * @param email E-mail a ser validado.
     */
    public static void validarEmail(String email){

        if (email.trim().isEmpty() || !email.contains("@") ||
                (!email.contains(".com") && !email.contains(".br") && !email.contains(".org"))) {
            throw new CustomException(MensagemErroUtils.EMAIL_INVALIDO);
        }

    }
}
