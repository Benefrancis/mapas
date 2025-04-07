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
@Table(name = "telefones_contato", schema = "uni_conservacao")
public class TelefoneContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telefone_contato_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "contato_id", referencedColumnName = "contato_id", foreignKey = @ForeignKey(name = "fk_CONTATO_TELEFONES"))
    @ToString.Exclude
    private Contatos contato;

    @Column(name = "uctel_numero", columnDefinition = "TEXT")
    private String numero;
}
