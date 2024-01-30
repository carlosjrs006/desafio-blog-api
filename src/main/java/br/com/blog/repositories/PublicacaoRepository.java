package br.com.blog.repositories;

import br.com.blog.models.Publicacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {

    @Query("SELECT p FROM Publicacao p ORDER BY p.dataCriacao DESC")
    List<Publicacao> findAllOrderedByDataCriacao();

    // Método com paginação
    Page<Publicacao> findAllByOrderByDataCriacaoDesc(Pageable pageable);
}
