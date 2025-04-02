package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "ppgr", schema = "uni_conservacao")
@Data
@Audited
public class Ppgr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppgr_id")
    private Long ppgrId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "full_name_ppgr", columnDefinition = "TEXT")
    private String fullNamePpgr;

    @Column(name = "nome_ppgiri", columnDefinition = "TEXT")
    private String nomePpgiri;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "nome_instrumento", columnDefinition = "TEXT")
    private String nomeInstrumento;

    @Column(name = "data_doc_legal")
    private LocalDate dataDocLegal;

    @Column(name = "doc_legal", columnDefinition = "TEXT")
    private String docLegal;

    @Column(name = "nome_contato_1", columnDefinition = "TEXT")
    private String nomeContato1;

    @Column(name = "tel_1", columnDefinition = "TEXT")
    private String tel1;

    @Column(name = "email_1", columnDefinition = "TEXT")
    private String email1;

    @Column(name = "website_1", columnDefinition = "TEXT")
    private String website1;

    @Column(name = "nome_contato_2", columnDefinition = "TEXT")
    private String nomeContato2;

    @Column(name = "tel_2", columnDefinition = "TEXT")
    private String tel2;

    @Column(name = "email_2", columnDefinition = "TEXT")
    private String email2;

    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;

    @Column(name = "tipo_nome", columnDefinition = "TEXT")
    private String tipoNome;

    @Column(name = "og_nome_ppgr", columnDefinition = "TEXT")
    private String ogNomePpgr;

    @Column(name = "esf_nome_ppgr", columnDefinition = "TEXT")
    private String esfNomePpgr;
}