package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "municipios_abrangidos", schema = "uni_conservacao")
public class MunAbrang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "municipio_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_MUNICIPIOS_ABRANGIDOS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "nome_municipio", columnDefinition = "TEXT")
    private String nomeMunicipio;

    @Column(name = "geocode_mun")
    private Integer geocodeMun;
}