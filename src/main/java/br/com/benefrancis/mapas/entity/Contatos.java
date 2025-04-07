package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Audited
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contatos",  schema = "uni_conservacao")
public class Contatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contato_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_CONTATOS"))
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

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    public Set<TelefoneContato> telefones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    public Set<EmailContato> emails = new LinkedHashSet<>();

}