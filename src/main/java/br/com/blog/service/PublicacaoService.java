package br.com.blog.service;

import br.com.blog.dtos.forms.ComentarioRequestDTO;
import br.com.blog.dtos.forms.ComentarioResponseDTO;
import br.com.blog.dtos.forms.PublicacaoRequestDTO;
import br.com.blog.dtos.forms.PublicacaoResponseDTO;
import br.com.blog.models.Comentarios;
import br.com.blog.models.Publicacao;

import java.util.List;
import java.util.Optional;

public interface PublicacaoService {

    PublicacaoResponseDTO salvarPublicacao(PublicacaoRequestDTO publicacaoRequestDto);
    List<Publicacao> buscarTodasPublicacoes();
    Optional<Publicacao> buscarPublicacaoPorId(Long id);
    Optional<Comentarios> buscarComentarioPorId(Long id);
    void deletarPublicacaoPorId(Long id, Optional<Publicacao> publicacao);

    void deletarComentarioPorId(Long id, Optional<Comentarios> comentarios);
    ComentarioResponseDTO salvarComentarios(ComentarioRequestDTO comentarioRequestDTO);

}
