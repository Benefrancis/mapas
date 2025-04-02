package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "social_networks_links", schema = "uni_conservacao")
@Data
@Audited
public class SocialNetworksLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "network_name", columnDefinition = "TEXT")
    private String networkName;

    @Column(name = "network_url", columnDefinition = "TEXT")
    private String networkUrl;
}