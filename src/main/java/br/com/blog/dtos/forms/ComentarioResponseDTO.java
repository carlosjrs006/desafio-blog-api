package br.com.blog.dtos.forms;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ComentarioResponseDTO {


    private Long idComentario;

    private String comentario;

    private String usuario;

    private LocalDateTime data;

    private Long idPublicacao;

}
