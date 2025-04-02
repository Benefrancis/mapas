package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "visitas", schema = "uni_conservacao")
@Data
@Audited
public class Visitas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucvisitas_id")
    private Long ucvisitasId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucvisitas_numero")
    private Integer ucvisitasNumero;

    @Column(name = "ucvisitas_ano")
    private Integer ucvisitasAno;

    @Column(name = "ucvisitas_numero_ano", columnDefinition = "TEXT")
    private String ucvisitasNumeroAno;
}