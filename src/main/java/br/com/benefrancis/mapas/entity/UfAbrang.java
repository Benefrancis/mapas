package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "ufs_abrangidas", schema = "uni_conservacao")
@Data
@Audited
public class UfAbrang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uf_id")
    private Long ufId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "nome_uf", columnDefinition = "TEXT")
    private String nomeUf;

    @Column(name = "geocode_uf")
    private Integer geocodeUf;
}