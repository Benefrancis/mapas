package br.com.benefrancis.mapas.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Set;

@Data
@Audited
@Entity
@Table(name = "unidade_conservacao", schema = "uni_conservacao")
public class UnidadeConservacao {

    @Id
    @Column(name = "uc_id")
    private Long id;

    @Column(name = "uc_cod_cnuc", columnDefinition = "TEXT")
    private String ucCodCnuc;

    @Column(name = "uc_nom", columnDefinition = "TEXT")
    private String ucNom;

    @Column(name = "uc_objetivo", columnDefinition = "TEXT")
    private String ucObjetivo;

    @Column(name = "dt_certificacao")
    private LocalDateTime dtCertificacao;

    @Column(name = "dt_aprovacao")
    private LocalDateTime dtAprovacao;

    @Column(name = "uc_plan_manejo_aprov")
    private Boolean ucPlanManejoAprov;

    @Column(name = "uc_existencia_conselho")
    private Boolean ucExistenciaConselho;

    @Column(name = "uc_outros_instrumentos")
    private Boolean ucOutrosInstrumentos;

    @Column(name = "ano_criacao", columnDefinition = "TEXT")
    private String anoCriacao;

    @Column(name = "cod_wdpa", columnDefinition = "TEXT")
    private String codWdpa;

    @Column(name = "og_nome", columnDefinition = "TEXT")
    private String ogNome;

    @Column(name = "esf_nome", columnDefinition = "TEXT")
    private String esfNome;

    @Column(name = "catmanej_nome", columnDefinition = "TEXT")
    private String catmanejNome;

    @Column(name = "catiucn_nome", columnDefinition = "TEXT")
    private String catiucnNome;

    @Column(name = "grumanej_nome", columnDefinition = "TEXT")
    private String grumanejNome;

    @Column(name = "sit_view_situacao", columnDefinition = "TEXT")
    private String sitViewSituacao;

    @Column(name = "effectivity", columnDefinition = "TEXT")
    private String effectivity;

    @Column(name = "in_shape_habilitado", columnDefinition = "TEXT")
    private String inShapeHabilitado;

    @Column(name = "pagina_uc", columnDefinition = "TEXT")
    private String paginaUc;

    @Column(name = "api_uc", columnDefinition = "TEXT")
    private String apiUc;

    @Column(name = "logo_uc", columnDefinition = "TEXT")
    private String logoUc;

    @Column(name = "geo_area_hectares_uc", columnDefinition = "TEXT")
    private String geoAreaHectaresUc;

    @Column(name = "geo_area_hectares_za", columnDefinition = "TEXT")
    private String geoAreaHectaresZa;

    @Column(name = "uc_sit_fund", columnDefinition = "TEXT")
    private String ucSitFund;

    @Column(name = "uc_pop_trad_ben_ou_res", columnDefinition = "TEXT")
    private String ucPopTradBenOuRes;

    private String areaAmazonia;
    private String areaCaatinga;
    private String areaCerrado;
    private String areaMataAtlantica;
    private String areaPampa;
    private String areaPantanal;
    private String areaMarinha;



    @Column(name = "ato_legal_criacao_full_name", columnDefinition = "TEXT")
    private String atoLegalCriacaoFullName;

    @Column(name = "ato_legal_criacao_link", columnDefinition = "TEXT")
    private String atoLegalCriacaoLink;

    @Column(name = "area_uc_ato_legal_criacao", columnDefinition = "TEXT")
    private String areaUcAtoLegalCriacao;

    @Column(name = "area_uc_ato_legal_recente", columnDefinition = "TEXT")
    private String areaUcAtoLegalRecente;

    @Column(name = "outros_atos_legais_full_name", columnDefinition = "TEXT")
    private String outrosAtosLegaisFullName;

    @Column(name = "outros_atos_legais_link", columnDefinition = "TEXT")
    private String outrosAtosLegaisLink;






    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OutrosInstrumentosGestao> outrosInstrumentosGestao = new LinkedHashSet<>();

    @OneToOne(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Contatos contatos;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<LegSolos> solos;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Emails> emails;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Telefone> telefones;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<AtosLegais> atosLegais;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<DocumentosConselho> docsConselho;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Bioma> biomas;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<LegSolos> legSolos;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Climas> climas;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Rios> rios;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Bacias> bacias;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UfAbrang> ufAbrang;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MunAbrang> munAbrang;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<GruSociais> gruSociais;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Videos> videos;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<RedesSociais> redesSociais;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Visitas> visitas;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Ppgr> ppgr;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UcSiteLinks> ucSiteLinks;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<SocialNetworksLinks> socialNetworksLinks;

    @OneToMany(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<PlanosManejo> planosDeManejo;

    @OneToOne(mappedBy = "unidadeConservacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Mapa mapa;
}