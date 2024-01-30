package br.com.blog.service.impl;

import br.com.blog.dtos.forms.*;
import br.com.blog.models.Comentarios;
import br.com.blog.models.Imagem;
import br.com.blog.models.Publicacao;
import br.com.blog.models.User;
import br.com.blog.repositories.ComentariosRepository;
import br.com.blog.repositories.PublicacaoRepository;
import br.com.blog.service.PublicacaoService;
import br.com.blog.service.StorageCloudinaryService;
import br.com.blog.utils.BlogUteis;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacaoServiceImpl implements PublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private BlogUteis blogUteis;

    @Autowired
    private StorageCloudinaryService storageCloudinaryService;

    @Override
    public PublicacaoResponseDTO salvarPublicacao(PublicacaoRequestDTO publicacaoRequestDto) {
        try {
            Publicacao publicacao = new Publicacao();
            User usuarioAtual = blogUteis.obterUsuarioAtual();
            if (usuarioAtual == null) {
                throw new RuntimeException("Error ao tentar salvar, por favor tente novamente!");
            }

            BeanUtils.copyProperties(publicacaoRequestDto, publicacao);
            publicacao.setImagens(new ArrayList<>());
            publicacao.setUsuario(usuarioAtual);
            publicacao.setDataCriacao(LocalDateTime.now());

            if (publicacaoRequestDto.getImagens() != null) {
                for (FileResponseDTO imagemDto : publicacaoRequestDto.getImagens()) {
                    Imagem imagem = new Imagem();
                    BeanUtils.copyProperties(imagemDto, imagem);
                    imagem.setPublicacao(publicacao);
                    publicacao.getImagens().add(imagem);
                }
            }
            Publicacao publicacaoRetorno = publicacaoRepository.save(publicacao);

            return getPublicacaoResponseDTO(publicacaoRetorno);

        } catch (Exception ex) {
            throw new RuntimeException("Error ao tentar salvar, por favor tente novamente!", ex);
        }

    }

    @Override
    public ComentarioResponseDTO salvarComentarios(ComentarioRequestDTO comentarioRequestDTO) {
        try {
        Comentarios comentarios = new Comentarios();
        User usuarioAtual = blogUteis.obterUsuarioAtual();

        Publicacao publicacao = buscarPublicacaoPorId(comentarioRequestDTO.getIdPostagem()).get();

        if(publicacao == null){
            throw new RuntimeException("Error ao tentar salvar, por favor tente novamente!");
        }

        if (usuarioAtual == null) {
            throw new RuntimeException("Error ao tentar salvar, por favor tente novamente!");
        }

        BeanUtils.copyProperties(comentarioRequestDTO, comentarios);
        comentarios.setPublicacao(publicacao);
        comentarios.setUsuario(usuarioAtual);
        comentarios.setDataCriacao(LocalDateTime.now());


        Comentarios comentariosRetorno = comentariosRepository.save(comentarios);

        return getComentariosResponseDTO(comentariosRetorno);
        } catch (Exception ex) {
            throw new RuntimeException("Error ao tentar salvar, por favor tente novamente!", ex);
        }
    }

    private static ComentarioResponseDTO getComentariosResponseDTO(Comentarios comentarios) {
        ComentarioResponseDTO comentarioResponseDTO = new ComentarioResponseDTO();
        comentarioResponseDTO.setIdComentario(comentarios.getIdComentario());
        comentarioResponseDTO.setComentario(comentarios.getComentario());
        comentarioResponseDTO.setUsuario(comentarios.getUsuario().getLogin());
        comentarioResponseDTO.setData(comentarios.getDataCriacao());
        comentarioResponseDTO.setIdPublicacao(comentarios.getPublicacao().getIdPublicacao());
        return comentarioResponseDTO;
    }

    private static PublicacaoResponseDTO getPublicacaoResponseDTO(Publicacao publicacaoRetorno) {
        PublicacaoResponseDTO publicacaoDTO = new PublicacaoResponseDTO();
        publicacaoDTO.setId(publicacaoRetorno.getIdPublicacao());
        publicacaoDTO.setTitulo(publicacaoRetorno.getTitulo());
        publicacaoDTO.setDescricao(publicacaoRetorno.getDescricao());
        publicacaoDTO.setImagens(publicacaoRetorno.getImagens());
        publicacaoDTO.setUsuario(publicacaoRetorno.getUsuario().getLogin());
        publicacaoDTO.setData(publicacaoRetorno.getDataCriacao());
        return publicacaoDTO;
    }

    @Override
    public List<Publicacao> buscarTodasPublicacoes() {

            try {
                return publicacaoRepository.findAllOrderedByDataCriacao();
            } catch (Exception e) {
                throw e;
            }
    }


    @Override
    public Optional<Publicacao> buscarPublicacaoPorId(Long id) {
        try {
            if(id == null) throw new RuntimeException("Error ao tentar buscar publicação, por favor tente novamente!");
            return publicacaoRepository.findById(id);
        } catch (Exception ex) {
            throw new RuntimeException("Error ao tentar buscar publicação, por favor tente novamente!", ex);
        }
    }

    @Override
    public Optional<Comentarios> buscarComentarioPorId(Long id) {
        try {
            if(id == null) throw new RuntimeException("Error ao tentar buscar comentario, por favor tente novamente!");
            return comentariosRepository.findById(id);
        } catch (Exception ex) {
            throw new RuntimeException("Error ao tentar buscar comentario, por favor tente novamente!", ex);
        }
    }

    @Override
    public void deletarComentarioPorId(Long id, Optional<Comentarios> comentarios) {
        comentariosRepository.deleteById(id);
    }

    @Override
    public void deletarPublicacaoPorId(Long id, Optional<Publicacao> publicacao) {

        publicacao.get().getImagens().forEach(imagem -> {
            try {
                storageCloudinaryService.delete(imagem.getPublicId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        publicacaoRepository.deleteById(id);
    }


}

