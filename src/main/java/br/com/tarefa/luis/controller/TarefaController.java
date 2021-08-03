package br.com.tarefa.luis.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tarefa.luis.controller.dto.TarefaDto;
import br.com.tarefa.luis.controller.form.TarefaForm;
import br.com.tarefa.luis.modelo.Tarefa;
import br.com.tarefa.luis.repository.TarefaRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/tarefas")
public class TarefaController {
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	
	@GetMapping
	@Cacheable(value = "listaDeTarefas")
	public Page<TarefaDto> lista(@RequestParam(required = false) String titulo, @RequestParam(required = false) String descricao,
			@PageableDefault(sort = "dataCriacao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		
		if (titulo == null || descricao == null) {
			Page<Tarefa> topicos = tarefaRepository.findAll(paginacao);
			return TarefaDto.converter(topicos);
		} else {
			Page<Tarefa> topicos = tarefaRepository.findByTituloContainingOrDescricaoContaining(titulo, descricao, paginacao);
			return TarefaDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTarefas", allEntries = true)
	public ResponseEntity<TarefaDto> cadastrar(@RequestBody @Valid TarefaForm form, UriComponentsBuilder uriBuilder) {
		Tarefa tarefa = form.converter();
		tarefaRepository.save(tarefa);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(tarefa.getId()).toUri();
		return ResponseEntity.created(uri).body(new TarefaDto(tarefa));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TarefaDto> detalhar(@PathVariable Long id) {
		Optional<Tarefa> tarefa = tarefaRepository.findById(id);
		if (tarefa.isPresent()) {
			return ResponseEntity.ok(new TarefaDto(tarefa.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTarefas", allEntries = true)
	public ResponseEntity<TarefaDto> atualizar(@PathVariable Long id, @RequestBody @Valid TarefaForm form) {
		Optional<Tarefa> optional = tarefaRepository.findById(id);
		if (optional.isPresent()) {
			tarefaRepository.save(optional.get());
			return ResponseEntity.ok(new TarefaDto(optional.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTarefas", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Tarefa> optional = tarefaRepository.findById(id);
		if (optional.isPresent()) {
			tarefaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}