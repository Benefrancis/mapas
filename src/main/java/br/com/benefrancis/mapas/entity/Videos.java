package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "videos", schema = "uni_conservacao")
@Data
@Audited
public class Videos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucvideos_id")
    private Long ucvideosId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucvideos_nome", columnDefinition = "TEXT")
    private String ucvideosNome;

    @Column(name = "ucvideos_link", columnDefinition = "TEXT")
    private String ucvideosLink;
}