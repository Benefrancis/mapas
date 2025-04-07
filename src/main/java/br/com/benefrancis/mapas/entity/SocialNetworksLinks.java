package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "social_networks_links", schema = "uni_conservacao")
public class SocialNetworksLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_LINK_REDES_SOCIAIS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "network_name", columnDefinition = "TEXT")
    private String networkName;

    @Column(name = "network_url", columnDefinition = "TEXT")
    private String networkUrl;
}