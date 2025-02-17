package br.com.benefrancis.mapas.dto;

import lombok.Builder;

@Builder
public record MunicipioDTO(Long id, String nome, EstadoDTO estado) {}
