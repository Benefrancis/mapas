package br.com.benefrancis.mapas.dto;

import lombok.Builder;

@Builder
public record PaisDTO(Long id, String nome) {}
