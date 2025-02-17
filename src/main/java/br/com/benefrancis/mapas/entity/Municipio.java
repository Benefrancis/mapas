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
@Table(name = "tb_municipios", schema = "mapas")
public class Municipio {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "gid")
    private Long id;

    @Column(name = "nm_mun", nullable = false)
    private String nome;


    @Column(name = "cod_mun")
    private String codigo;

    @Column(name = "cd_rgi")
    private String codigoRegiaoImediata;
    @Column(name = "nm_rgi")
    private String nomeRegiaoImediata;


    @Column(name = "cd_rgint")
    private String codigoRegiaoIntermediaria;
    @Column(name = "nm_rgint")
    private String nomeRegiaoIntermediaria;

    @Column(name = "cd_uf")
    private String codigoUF;
    @Column(name = "nm_uf")
    private String nomeUF;


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
