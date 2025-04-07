package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Table(name = "bacias", schema = "uni_conservacao")
public class Bacias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bacia_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_BACIAS"))
    @ToString.Exclude //Avoid circular reference
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "bacia", columnDefinition = "TEXT")
    private String bacia;
}