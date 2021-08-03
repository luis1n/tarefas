package br.com.tarefa.luis.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


import br.com.tarefa.luis.modelo.Tarefa;


public class TarefaForm {

	@NotBlank
	private String titulo;
	
	@NotBlank
	private String descricao;
	
	@Pattern(regexp = "ABERTA|EM_ANDAMENTO|CONCLUIDA", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String status;
	
	public TarefaForm(String titulo, String descricao, String status) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.status = status;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public Tarefa converter(){
		if (status != null) {
			return new Tarefa(titulo, descricao, status);
		}
		return new Tarefa(titulo, descricao);
	}
	
	
	
}
