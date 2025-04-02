package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "uc_site_links", schema = "uni_conservacao")
@Data
@Audited
public class UcSiteLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "link_name", columnDefinition = "TEXT")
    private String linkName;

    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;
}