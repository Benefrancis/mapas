package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "rios", schema = "uni_conservacao")
public class Rios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rio_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_REDES_RIOS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "rio", columnDefinition = "TEXT")
    private String rio;
}