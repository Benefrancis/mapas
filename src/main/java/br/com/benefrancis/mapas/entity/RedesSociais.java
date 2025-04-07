package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "redes_sociais", schema = "uni_conservacao")
public class RedesSociais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucsocial_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_REDES_SOCIAIS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucsocial_nome", columnDefinition = "TEXT")
    private String ucsocialNome;

    @Column(name = "ucsocial_link", columnDefinition = "TEXT")
    private String ucsocialLink;
}