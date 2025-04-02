package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "atos_legais", schema = "uni_conservacao")
@Data
@Audited
public class AtosLegais {

    @Id
    @Column(name = "docleg_id")
    private Integer doclegId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "docleg_classdoc_id")
    private Integer doclegClassdocId;

    @Column(name = "docleg_numero", columnDefinition = "TEXT")
    private String doclegNumero;

    @Column(name = "docleg_link", columnDefinition = "TEXT")
    private String doclegLink;

    @Column(name = "docleg_link_2", columnDefinition = "TEXT")
    private String doclegLink2;

    @Column(name = "docleg_dt_doc")
    private LocalDate doclegDtDoc;

    @Column(name = "docleg_dt_publicacao")
    private LocalDate doclegDtPublicacao;

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

    @Column(name = "tipo", columnDefinition = "TEXT")
    private String tipo;
}