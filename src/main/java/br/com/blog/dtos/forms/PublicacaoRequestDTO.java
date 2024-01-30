package br.com.blog.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacaoRequestDTO {

    private Long id;

    @NotBlank(message = "titulo obrigatorio, por favor preencha!")
    private String titulo;

    @NotBlank(message = "descricao obrigatorio, por favor preencha!")
    private String descricao;

    private List<FileResponseDTO> imagens;

}
