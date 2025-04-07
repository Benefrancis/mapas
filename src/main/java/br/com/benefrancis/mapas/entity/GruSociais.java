package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "grupos_sociais", schema = "uni_conservacao")
public class GruSociais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grusocial_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_GRUPOS_SOCIAIS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "grusocial_nome", columnDefinition = "TEXT")
    private String grusocialNome;
}