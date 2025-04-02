package br.com.benefrancis.mapas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "municipios_abrangidos", schema = "uni_conservacao")
@Data
@Audited
public class MunAbrang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "municipio_id")
    private Long municipioId;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConservacao unidadeConservacao;

    @Column(name = "nome_municipio", columnDefinition = "TEXT")
    private String nomeMunicipio;

    @Column(name = "geocode_mun")
    private Integer geocodeMun;
}