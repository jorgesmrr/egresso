package br.ufjf.egresso.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "postagem")
public class Postagem {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;

	// sempre será preciso saber o autor da postagem
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "aluno_id", nullable = false)
	private Aluno aluno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turma_id", nullable = false)
	private Turma turma;

	// apenas será visto por ex-alunos daquela turma?
	@Column(name = "privado", nullable = false)
	private boolean privado;

	@Column(name = "texto", nullable = true)
	private String texto;

	@Column(name = "imagem", nullable = true)
	private String imagem;
	@Column(name = "thumbnail", nullable = true)
	private String tbnail;

	@Column(name = "data_hora", nullable = false)
	private Timestamp dataHora;
	@Transient
	private boolean podeEditar = false;
	public boolean isPodeEditar() {
		return podeEditar;
	}

	public void setPodeEditar(boolean podeEditar) {
		this.podeEditar = podeEditar;
	}

	public Postagem() {
		super();
	}

	public Postagem(Aluno aluno, Turma turma, boolean privado, String texto,
			String imagem, Timestamp dataHora, String tbnail) {
		super();
		this.aluno = aluno;
		this.turma = turma;
		this.privado = privado;
		this.texto = texto;
		this.imagem = imagem;
		this.dataHora = dataHora;
		this.tbnail = tbnail;
	}

	public String getTbnail() {
		return tbnail;
	}

	public void setTbnail(String tbnail) {
		this.tbnail = tbnail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public boolean isPrivado() {
		return privado;
	}

	public void setPrivado(boolean privado) {
		this.privado = privado;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public Timestamp getDataHora() {
		return dataHora;
	}

	public void setDataHora(Timestamp dataHora) {
		this.dataHora = dataHora;
	}

	

}