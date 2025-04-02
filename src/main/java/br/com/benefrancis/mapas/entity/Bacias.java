package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "bacias", schema = "uni_conservacao")
@Data
@Audited
public class Bacias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bacia_id")
    private Long baciaId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "bacia", columnDefinition = "TEXT")
    private String bacia;
}