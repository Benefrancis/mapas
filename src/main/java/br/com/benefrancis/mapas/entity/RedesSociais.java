package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "redes_sociais", schema = "uni_conservacao")
@Data
@Audited
public class RedesSociais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucsocial_id")
    private Long ucsocialId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucsocial_nome", columnDefinition = "TEXT")
    private String ucsocialNome;

    @Column(name = "ucsocial_link", columnDefinition = "TEXT")
    private String ucsocialLink;
}