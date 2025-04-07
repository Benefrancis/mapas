package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "biomas", schema = "uni_conservacao")
public class Bioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bioma_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_BIOMAS"))
    @ToString.Exclude //Avoid circular reference
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "bioma", columnDefinition = "TEXT")
    private String bioma;

    @Column(name = "area_bioma_ha")
    private String areaBiomaHa;
}