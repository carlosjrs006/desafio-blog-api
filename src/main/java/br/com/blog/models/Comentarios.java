package br.com.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "comentarios")
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "seq_comentario", sequenceName = "seq_comentario", allocationSize = 1, initialValue = 1)
public class Comentarios implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comentario")
    private Long idComentario;

    @Column
    private String comentario;

    @JsonIgnore
    @JsonIgnoreProperties("publicacao")
    @ManyToOne(targetEntity = Publicacao.class)
    @JoinColumn(name = "publicacao_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "publicacao_fk"))
    private Publicacao publicacao;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "usuario_id_fk"))
    private User usuario;
}
