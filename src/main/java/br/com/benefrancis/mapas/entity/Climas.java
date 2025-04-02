package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "climas", schema = "uni_conservacao")
@Data
@Audited
public class Climas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clima_id")
    private Long climaId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "clima", columnDefinition = "TEXT")
    private String clima;
}