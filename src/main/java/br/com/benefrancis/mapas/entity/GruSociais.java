package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "grupos_sociais", schema = "uni_conservacao")
@Data
@Audited
public class GruSociais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grusocial_id")
    private Long grusocialId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "grusocial_nome", columnDefinition = "TEXT")
    private String grusocialNome;
}