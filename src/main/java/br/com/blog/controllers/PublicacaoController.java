package br.com.blog.controllers;

import br.com.blog.dtos.forms.ComentarioRequestDTO;
import br.com.blog.dtos.forms.ComentarioResponseDTO;
import br.com.blog.dtos.forms.PublicacaoRequestDTO;
import br.com.blog.dtos.forms.PublicacaoResponseDTO;
import br.com.blog.models.Comentarios;
import br.com.blog.models.Publicacao;
import br.com.blog.models.User;
import br.com.blog.service.PublicacaoService;
import br.com.blog.utils.BlogUteis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
@CrossOrigin(origins = "https://main--coruscating-genie-83c44c.netlify.app")
@RestController
@RequestMapping("/publicacao")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;

    public List<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    @Autowired
    private BlogUteis blogUteis;

    @PostMapping
    public ResponseEntity<?> salvarPublicacao(@RequestBody @Valid PublicacaoRequestDTO publicacaoRequestDto){

        try{

            PublicacaoResponseDTO publicacao = publicacaoService.salvarPublicacao(publicacaoRequestDto);
            return ResponseEntity.ok().body(publicacao);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> salvarComentarios(@RequestBody @Valid ComentarioRequestDTO comentarioRequestDTO){

        try{

            ComentarioResponseDTO comentarios = publicacaoService.salvarComentarios(comentarioRequestDTO);

            for (SseEmitter sseEmitter :sseEmitters){
                try{
                    sseEmitter.send(SseEmitter.event().name("novo-comentarios").data(comentarios));
                }catch (IOException e){
                    sseEmitters.remove(sseEmitter);
                }
            }
            return ResponseEntity.ok().body(comentarios);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscriber() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitter));
        sseEmitters.add(sseEmitter);
        return sseEmitter;
    }
    @GetMapping
    public ResponseEntity<List<?>> buscarTodasPublicacoes(){
        try{
            return ResponseEntity.ok().body(publicacaoService.buscarTodasPublicacoes());
        }catch (Exception e){
            throw e;
        }

    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> buscarComentarioPorId(
            @PathVariable("id") Long id){

        Optional<Publicacao> publicacao = publicacaoService.buscarPublicacaoPorId(id);

        if (publicacao.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("publicação não econtrada!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(publicacao);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deletarComentarioPorId(@PathVariable("id") Long id){

        Optional<Comentarios> optionalComentarios = publicacaoService.buscarComentarioPorId(id);

        User usuarioAtual = blogUteis.obterUsuarioAtual();

        if (optionalComentarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentario não encontrada, tente mais tarde ou recarregar a pagina!!");
        }

        if (!optionalComentarios.get().getUsuario().equals(usuarioAtual)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sem permissão para excluir esse comentario, voce não é o autor dessa postagem!!");
        }

        publicacaoService.deletarComentarioPorId(optionalComentarios.get().getIdComentario(), optionalComentarios);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPublicacaoPorId(@PathVariable("id") Long id){

        Optional<Publicacao> publicacaoModelOptional = publicacaoService.buscarPublicacaoPorId(id);

        User usuarioAtual = blogUteis.obterUsuarioAtual();

        if (publicacaoModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicação não encontrada, tente mais tarde ou recarregar a pagina!!");
        }

        if (!publicacaoModelOptional.get().getUsuario().equals(usuarioAtual)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sem permissão para excluir essa publicação, voce não é o autor dessa postagem!!");
        }

        publicacaoService.deletarPublicacaoPorId(publicacaoModelOptional.get().getIdPublicacao(), publicacaoModelOptional);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
