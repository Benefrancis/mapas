package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "climas", schema = "uni_conservacao")
public class Climas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clima_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_CLIMAS"))
    @ToString.Exclude //Avoid circular reference
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "clima", columnDefinition = "TEXT")
    private String clima;
}