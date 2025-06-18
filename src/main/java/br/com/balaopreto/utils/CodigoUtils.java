package br.com.balaopreto.utils;

public class CodigoUtils {

    public static int gerarCodigo4Digitos() {
        return (int)(Math.random() * 9000) + 1000;
    }
}
