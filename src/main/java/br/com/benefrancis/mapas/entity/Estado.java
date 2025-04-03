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
@Table(name = "tb_uf", schema = "funai")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gid")
    private Long id;

    @Column(name = "cd_uf")
    private String codigo;

    @Column(name = "sigla_uf")
    private String sigla;

    @Column(name = "cd_regiao")
    private String codigoRegiao;

    @Column(name = "nm_regiao")
    private String nomeRegiao;

    @Column(name = "area_km2")
    private Float area;


    @JsonIgnore // Evita carregar os dados espaciais por padrão
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon geom;
}
