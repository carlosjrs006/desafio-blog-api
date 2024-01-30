package br.com.blog.dtos.forms;

import br.com.blog.models.Imagem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PublicacaoResponseDTO {

    private Long id;

    private String titulo;

    private String descricao;

    private List<Imagem> imagens;

    private String usuario;

    private LocalDateTime data;

}
