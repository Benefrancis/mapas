package br.com.benefrancis.mapas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.MultiPolygon;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_pais", schema = "mapas")
public class Pais {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "gid")
    private Long id;

    @Column(name = "nm_pais", nullable = false)
    private String nome;

    @Column(name = "area_km2")
    private Float area;


    @JsonIgnore // Evita carregar os dados espaciais por padrão
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon geom;
}
