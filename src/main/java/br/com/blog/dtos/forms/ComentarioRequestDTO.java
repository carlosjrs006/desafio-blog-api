package br.com.blog.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioRequestDTO {


    @NotBlank(message = "comentario obrigatorio, por favor preencha!")
    private String comentario;

    @NotNull
    private Long idPostagem;

}
