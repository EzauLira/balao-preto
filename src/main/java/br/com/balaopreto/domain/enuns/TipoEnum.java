package br.com.balaopreto.domain.enuns;

public enum TipoEnum {

    LOGIN("Login"),
    CADASTRO("Cadastro");

    private String tipo;

    TipoEnum(String tipo){
        this.tipo = tipo;
    }

    public String getTipo(){
        return tipo;
    }
}
