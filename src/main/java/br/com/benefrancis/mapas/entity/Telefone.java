package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Data
@Audited
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telefones", schema = "uni_conservacao")
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telefone_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", foreignKey = @ForeignKey(name = "fk_UC_TELEFONES"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "uctel_numero", columnDefinition = "TEXT")
    private String uctelNumero;
}