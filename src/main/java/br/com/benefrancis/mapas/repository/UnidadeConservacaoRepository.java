package br.com.benefrancis.mapas.repository;

import br.com.benefrancis.mapas.entity.UnidadeConservacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeConservacaoRepository extends JpaRepository<UnidadeConservacao, Integer> {
    // Métodos personalizados podem ser adicionados aqui
}