package br.com.benefrancis.mapas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.locationtech.jts.geom.MultiPolygon;

@Data
@Audited
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mapas", schema = "uni_conservacao")
public class Mapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapa_id")
    private Long id;

    @Column(name = "link_mapa_uc", columnDefinition = "TEXT" )
    private String linkMapaUc;

    @Column(name = "qual_dado_geoespacial", columnDefinition = "TEXT" )
    private String qualDadoGeoespacial;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "uc_id", referencedColumnName = "uc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UC_MAPAS"))
    @ToString.Exclude //Avoid circular reference
    private UnidadeConservacao unidadeConservacao;

    @JsonIgnore // Evita carregar os dados espaciais por padrão
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon geom;

}
