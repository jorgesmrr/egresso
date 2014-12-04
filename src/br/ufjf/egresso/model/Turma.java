package br.ufjf.egresso.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "turma")
public class Turma {
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;

	@Column(name = "semestre", unique = true, length = 1, nullable = false)
	private int semestre;

	@Column(name = "ano", unique = true, length = 4, nullable = false)
	private int ano;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
	private List<Aluno> alunos = new ArrayList<Aluno>();
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "curso_id", nullable = false)
	private Curso curso;

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Transient
	private boolean editingStatus;

	public int getSemestre() {
		return semestre;
	}

	public boolean getEditingStatus() {
		return editingStatus;
	}

	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public void copy(Turma outra) {
		this.id = outra.id;
		this.semestre = outra.semestre;
		this.alunos = outra.alunos;
		this.ano = outra.ano;

	}
}
