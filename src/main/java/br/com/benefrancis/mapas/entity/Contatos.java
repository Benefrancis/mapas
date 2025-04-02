package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "contatos")
@Data
@Audited
public class Contatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contato_id")
    private Long contatoId;

    @OneToOne
    @JoinColumn(name = "uc_id", nullable = false, unique = true)
    @ToString.Exclude //Avoid circular reference
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "end_cep", columnDefinition = "TEXT")
    private String endCep;

    @Column(name = "end_uf", columnDefinition = "TEXT")
    private String endUf;

    @Column(name = "end_municipio", columnDefinition = "TEXT")
    private String endMunicipio;

    @Column(name = "end_bairro", columnDefinition = "TEXT")
    private String endBairro;

    @Column(name = "end_logradouro", columnDefinition = "TEXT")
    private String endLogradouro;

    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;

    @Column(name = "uc_site", columnDefinition = "TEXT")
    private String ucSite;
}