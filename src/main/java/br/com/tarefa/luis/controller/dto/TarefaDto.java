package br.com.tarefa.luis.controller.dto;


import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;

import br.com.tarefa.luis.modelo.Tarefa;

public class TarefaDto {
	
	private DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private Long id;
	private String titulo;
	private String descricao;
	private String dataCriacao;
		
	public TarefaDto(Tarefa tarefa) {
		this.id = tarefa.getId();
		this.titulo = tarefa.getTitulo();
		this.descricao = tarefa.getDescricao();
		this.dataCriacao = tarefa.getDataCriacao().minusHours(3).format(date).toString();
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}	

	public static Page<TarefaDto> converter(Page<Tarefa> tarefas) {
		return tarefas.map(TarefaDto::new);
	}

}
