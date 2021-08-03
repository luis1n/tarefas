package br.com.tarefa.luis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tarefa.luis.modelo.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

	Page<Tarefa> findByTituloContainingOrDescricaoContaining(String titulo, String descricao, Pageable paginacao);
	
}
