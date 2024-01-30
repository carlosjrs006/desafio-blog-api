package br.com.blog.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "imagem")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "seq_image", sequenceName = "seq_image", allocationSize = 1, initialValue = 1)
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_image")
    private Long idImagem;

    @Column
    private String publicId;

    @Column
    private String nome;

    @Column
    private String url;

    @JsonBackReference
    @ManyToOne(targetEntity = Publicacao.class)
    @JoinColumn(name = "publicacao_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "publicacao_fk"))
    private Publicacao publicacao;

}