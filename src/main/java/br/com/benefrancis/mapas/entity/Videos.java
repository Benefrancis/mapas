package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "videos", schema = "uni_conservacao")
public class Videos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ucvideos_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_VIDEOS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucvideos_nome", columnDefinition = "TEXT")
    private String ucvideosNome;

    @Column(name = "ucvideos_link", columnDefinition = "TEXT")
    private String ucvideosLink;
}