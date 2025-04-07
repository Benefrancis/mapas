package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "visitas", schema = "uni_conservacao")
public class Visitas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucvisitas_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_VISITAS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucvisitas_numero")
    private Integer ucvisitasNumero;

    @Column(name = "ucvisitas_ano")
    private Integer ucvisitasAno;

    @Column(name = "ucvisitas_numero_ano", columnDefinition = "TEXT")
    private String ucvisitasNumeroAno;
}