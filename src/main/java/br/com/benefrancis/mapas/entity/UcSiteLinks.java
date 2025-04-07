package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "uc_site_links", schema = "uni_conservacao")
public class UcSiteLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_LINK_SITES"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "link_name", columnDefinition = "TEXT")
    private String linkName;

    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;
}