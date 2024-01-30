package br.com.blog.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "publicacao")
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "seq_publicacao", sequenceName = "seq_publicacao", allocationSize = 1, initialValue = 1)
public class Publicacao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_publicacao")
    private Long idPublicacao;

    @Column
    private String titulo;

    @Column
    private String descricao;


    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "usuario_id_fk"))
    private User usuario;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;


    @OneToMany(mappedBy = "publicacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Imagem> imagens;

    @OneToMany(mappedBy = "publicacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderBy("dataCriacao DESC")
    private List<Comentarios> comentarios;




}
