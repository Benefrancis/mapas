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
@Table(name = "emails", schema = "uni_conservacao")
public class Emails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id;

    @ManyToOne  (fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", foreignKey = @ForeignKey(name = "fk_UC_EMAILS"))
    @ToString.Exclude
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "ucema_email", columnDefinition = "TEXT")
    private String ucemaEmail;
}