package br.com.benefrancis.mapas.dto;

import lombok.Builder;

@Builder
public record EstadoDTO(Long id, String nome, PaisDTO pais) {
}
