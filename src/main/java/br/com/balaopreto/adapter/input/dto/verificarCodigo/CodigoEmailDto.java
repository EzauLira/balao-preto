package br.com.balaopreto.adapter.input.dto.verificarCodigo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoEmailDto {

    private int codigo;
    private String email;

}
