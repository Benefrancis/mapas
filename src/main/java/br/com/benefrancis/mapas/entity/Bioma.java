package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "biomas", schema = "uni_conservacao")
@Data
@Audited
public class Bioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bioma_id")
    private Long biomaId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "bioma", columnDefinition = "TEXT")
    private String bioma;

    @Column(name = "area_bioma_ha")
    private Double areaBiomaHa;
}