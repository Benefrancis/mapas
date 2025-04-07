package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.util.Date;

@Data
@Audited
@Entity
@Builder
@Table(name = "planos_manejo", schema = "uni_conservacao")
public class PlanosManejo {

    @Id
    @Column(name = "docleg_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", foreignKey = @ForeignKey(name = "fk_UC_PLANOS_MANEJO"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;


    @Column(name = "plano_manejo_full_name", columnDefinition = "TEXT")
    private String planoManejoFullName;

    @Column(name = "plano_manejo_links", columnDefinition = "TEXT")
    private String planoManejoLinks;

    @Column(name = "docleg_classdoc_id")
    private Integer doclegClassdocId;

    @Column(name = "docleg_numero", columnDefinition = "TEXT")
    private String doclegNumero;

    @Column(name = "docleg_link", columnDefinition = "TEXT")
    private String doclegLink;

    @Column(name = "docleg_link_2", columnDefinition = "TEXT")
    private String doclegLink2;

    @Column(name = "docleg_dt_doc")
    private Date doclegDtDoc;

    @Column(name = "docleg_dt_publicacao")
    private Date doclegDtPublicacao;

    @Column(name = "docleg_nu_area_documento", columnDefinition = "TEXT")
    private String doclegNuAreaDocumento;

    @Column(name = "tipodoc_nome", columnDefinition = "TEXT")
    private String tipodocNome;

    @Column(name = "instpub_nome", columnDefinition = "TEXT")
    private String instpubNome;

    @Column(name = "docfin_nome", columnDefinition = "TEXT")
    private String docfinNome;

    @Column(name = "docfin_id")
    private Integer docfinId;

    @Column(name = "tipo_inst_nome", columnDefinition = "TEXT")
    private String tipoInstNome;
}