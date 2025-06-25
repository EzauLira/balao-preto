package br.com.balaopreto.utils.validadores;

import br.com.balaopreto.utils.constantes.MensagemErroUtils;
import br.com.balaopreto.domain.exception.CustomException;

/**
 * Classe utilitária para validação de telefones.
 */
public class ValidarTelefoneUtils {

    /**
     * Valida o telefone fornecido.
     * Verifica se o DDD é válido (01 a 99)
     * Verifica se o primeiro dígito do número é 9 (para celulares)
     * Verifica se o número não é composto por todos os mesmos dígitos
     * Lança uma exceção se o telefone for inválido.
     *
     * @param telefoneSemFormatacao Telefone vindo ser formatação para ser validado.
     */
    public static void validarTelefone(String telefoneSemFormatacao) {
        if (telefoneSemFormatacao == null || telefoneSemFormatacao.trim().isEmpty()) {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }

        String telefone = telefoneSemFormatacao.replaceAll("[\\s\\-().]", "");

        if (telefone.length() != 11) {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }

        if (!telefone.matches("\\d{11}")) {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }

        String ddd = telefone.substring(0, 2);
        if (Integer.parseInt(ddd) < 1 || Integer.parseInt(ddd) > 99) {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }

        if (telefone.charAt(2) != '9') {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }

        if (telefone.chars().distinct().count() == 1) {
            throw new CustomException(MensagemErroUtils.TELEFONE_INVALIDO);
        }
    }

}