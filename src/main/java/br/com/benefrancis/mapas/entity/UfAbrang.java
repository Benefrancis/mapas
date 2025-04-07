package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;


@Data
@Audited
@Entity
@Table(name = "ufs_abrangidas", schema = "uni_conservacao")
public class UfAbrang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uf_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_UFS_ABRANGIDAS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "nome_uf", columnDefinition = "TEXT")
    private String nomeUf;

    @Column(name = "geocode_uf")
    private Integer geocodeUf;
}