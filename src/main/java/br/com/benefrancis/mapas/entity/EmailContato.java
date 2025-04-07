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
@Table(name = "emails_contato", schema = "uni_conservacao")
public class EmailContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_contato_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", foreignKey = @ForeignKey(name = "fk_CONTATO_EMAILS"))
    @ToString.Exclude
    private Contatos contato;

    @Column(name = "ucema_email", columnDefinition = "TEXT")
    private String email;

}
