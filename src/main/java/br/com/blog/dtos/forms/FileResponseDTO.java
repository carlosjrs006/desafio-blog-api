package br.com.blog.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {

    private String publicId;
    private String nome;
    private String url;

}
