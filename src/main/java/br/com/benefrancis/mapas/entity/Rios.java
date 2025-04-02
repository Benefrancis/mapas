package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "rios", schema = "uni_conservacao")
@Data
@Audited
public class Rios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rio_id")
    private Long rioId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "rio", columnDefinition = "TEXT")
    private String rio;
}