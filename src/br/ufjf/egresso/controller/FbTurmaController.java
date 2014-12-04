package br.ufjf.egresso.controller;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufjf.egresso.business.AlunoBusiness;
import br.ufjf.egresso.business.CursoBusiness;
import br.ufjf.egresso.business.PostagemBusiness;
import br.ufjf.egresso.business.TurmaBusiness;
import br.ufjf.egresso.model.Aluno;
import br.ufjf.egresso.model.Atuacao;
import br.ufjf.egresso.model.Curso;
import br.ufjf.egresso.model.Postagem;
import br.ufjf.egresso.model.TipoAtuacao;
import br.ufjf.egresso.model.Turma;
import br.ufjf.egresso.utils.ConfHandler;
import br.ufjf.egresso.utils.FileManager;

/**
 * Classe para controlar a página /fb/turma.zul , /fb/todosAlunos.zul ,
 * /fb/albumTurma.zul
 * 
 * @author Jorge Augusto da Silva Moreira e Eduardo Rocha Soares
 * 
 */
public class FbTurmaController {

	private List<Aluno> alunos, filtraAlunos;
	private List<List<Aluno>> linhasAluno;
	private List<List<Postagem>> linhasPostagem;
	private List<String> semestres;
	private List<Turma> turmas;
	private TurmaBusiness turmaBusiness = new TurmaBusiness();
	private String pesquisa, descricao, imgExtensao;
	// turma do aluno
	private Turma turma;
	// aluno responsável pela sessão
	private Aluno aluno;
	private List<Postagem> postagensTurma;
	private int largura, altura;
	private InputStream imgPostagem;
	private ArrayList<String> urlPostagens;
	private int indiceImagem;
	private boolean buscaGlobal = false;
	private String nomeImg;
	// curso do aluno da sessão
	private Curso curso;
	private CursoBusiness cursoBusiness;
	// turma selecionada atualmente
	private String turmaSelecionada;
	// curso selecionado atualmente
	private Curso cursoSelecionado;
	private List<Aluno> todosAlunos;

	public CursoBusiness getCursoBusiness() {
		return cursoBusiness;
	}

	public void setCursoBusiness(CursoBusiness cursoBusiness) {
		this.cursoBusiness = cursoBusiness;
	}

	public String getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(String turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}

	public Curso getCursoSelecionado() {
		return cursoSelecionado;
	}

	public void setCursoSelecionado(Curso cursoSelecionado) {
		this.cursoSelecionado = cursoSelecionado;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	private Aluno alunoSelect;
	private List<Curso> cursos = new CursoBusiness().getTodos();

	public Aluno getAlunoSelect() {
		return alunoSelect;
	}

	public void setAlunoSelect(Aluno alunoSelect) {
		this.alunoSelect = alunoSelect;
	}

	@Init
	public void init() {
		BindUtils.postNotifyChange(null, null, this, "alunoSelect");
		/*
		 * pega aluno e turma passados por get da página index.zul
		 */
		turma = ((Aluno) Sessions.getCurrent().getAttribute("aluno"))
				.getTurma();
		aluno = ((Aluno) Sessions.getCurrent().getAttribute("aluno"));

		turmaSelecionada = Integer.toString(turma.getAno()) + ' '
				+ Integer.toString(turma.getSemestre()) + 'º' + ' '
				+ "semestre";
		cursoSelecionado = aluno.getCurso();
		trocaTurma();

		turmas = turmaBusiness.getTodas();

		semestres = new ArrayList<String>();
		for (Turma t : turmas) {
			semestres.add(t.getAno() + " " + t.getSemestre() + "º semestre");
		}

		descricao = "Turma do " + turma.getSemestre() + "º semestre de "
				+ turma.getAno();
		filtraAlunos = new AlunoBusiness().getAlunos(turma);
		alunos = new AlunoBusiness().getTodosCurso(aluno.getCurso());

		todosAlunos = new AlunoBusiness().getTodos();
		postagensTurma = new PostagemBusiness().getPostagens(turma);
		urlPostagens = new ArrayList<String>();
		for (int i = 0; i < postagensTurma.size(); i++) {
			if (postagensTurma.get(i).getImagem() != null)
				urlPostagens.add(postagensTurma.get(i).getImagem());
			if (postagensTurma.get(i).getAluno().getId() == aluno.getId()) {
				postagensTurma.get(i).setPodeEditar(true);
			}
		}
		/*
		 * for(String s : urlPostagens){
		 * 
		 * System.out.println(s); }
		 */

	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Aluno> getFiltraAlunos() {
		return filtraAlunos;
	}

	public void setFiltraAlunos(List<Aluno> filtraAlunos) {
		this.filtraAlunos = filtraAlunos;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public TurmaBusiness getTurmaBusiness() {
		return turmaBusiness;
	}

	public void setTurmaBusiness(TurmaBusiness turmaBusiness) {
		this.turmaBusiness = turmaBusiness;
	}

	public String getImgExtensao() {
		return imgExtensao;
	}

	public void setImgExtensao(String imgExtensao) {
		this.imgExtensao = imgExtensao;
	}

	public InputStream getImgPostagem() {
		return imgPostagem;
	}

	public void setImgPostagem(InputStream imgPostagem) {
		this.imgPostagem = imgPostagem;
	}

	public ArrayList<String> getUrlPostagens() {
		return urlPostagens;
	}

	public void setUrlPostagens(ArrayList<String> urlPostagens) {
		this.urlPostagens = urlPostagens;
	}

	public int getIndiceImagem() {
		return indiceImagem;
	}

	public void setIndiceImagem(int indiceImagem) {
		this.indiceImagem = indiceImagem;
	}

	public void setSemestres(List<String> semestres) {
		this.semestres = semestres;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public void setPostagensTurma(List<Postagem> postagensTurma) {
		this.postagensTurma = postagensTurma;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public int getAltura() {
		return altura;
	}

	public void removeFromList(Postagem postagem) {

		postagensTurma.remove(postagem);
		BindUtils.postNotifyChange(null, null, this, "postagensTurma");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void excluirPostagem(
			@BindingParam("postagem") final Postagem postagem) {
		Messagebox.show("Você tem certeza que deseja excluir a postagem "
				+ postagem.getTexto() + "?", "Confirmação", Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (new PostagemBusiness().exclui(postagem))
								removeFromList(postagem);

						}
					}
				});
	}

	/**
	 * Monta a tabela de alunos da página de turma
	 * 
	 * @param evt
	 *            Evento disparado pelo ator {@link Aluno}
	 * */
	@Command
	public void montaTabela(@BindingParam("event") ClientInfoEvent evt) {

		if (evt != null) {
			largura = evt.getDesktopWidth();
			altura = evt.getDesktopHeight() - 180;

			BindUtils.postNotifyChange(null, null, this, "largura");
			BindUtils.postNotifyChange(null, null, this, "altura");
		}
		int inseridos = 0;
		linhasAluno = new ArrayList<List<Aluno>>();

		List<Aluno> linhaAluno = new ArrayList<Aluno>();

		for (Aluno a : filtraAlunos) {
			if ((largura - 300) / 180 < (inseridos + 1)) {
				linhasAluno.add(linhaAluno);
				inseridos = 0;
				linhaAluno = new ArrayList<Aluno>();
			}
			// adiciona os alunos na lista
			linhaAluno.add(a);
			inseridos++;

		}

		if (linhaAluno.size() > 0)
			linhasAluno.add(linhaAluno);

		BindUtils.postNotifyChange(null, null, this, "postagensTurma");
		BindUtils.postNotifyChange(null, null, this, "linhasAluno");
		Clients.evalJavaScript("fadeIn()");
	}

	/**
	 * Monta a tabela da página de todos alunos
	 * 
	 * @param evt
	 *            Evento disparado pelo ator
	 */
	@Command
	public void montaTabelaTodos(@BindingParam("event") ClientInfoEvent evt) {
		if (evt != null) {
			largura = evt.getDesktopWidth();
			altura = evt.getDesktopHeight() - 180;

			BindUtils.postNotifyChange(null, null, this, "largura");
			BindUtils.postNotifyChange(null, null, this, "altura");
		}
		filtraAlunos = new AlunoBusiness().getTodosCurso(cursoSelecionado);

		int inseridos = 0;
		linhasAluno = new ArrayList<List<Aluno>>();
		List<Aluno> linhaAluno = new ArrayList<Aluno>();

		for (Aluno a : filtraAlunos) {
			if ((largura - 120) / 160 < (inseridos + 1)) {
				linhasAluno.add(linhaAluno);
				inseridos = 0;
				linhaAluno = new ArrayList<Aluno>();
			}
			linhaAluno.add(a);
			inseridos++;

		}

		if (linhaAluno.size() > 0)
			linhasAluno.add(linhaAluno);

		// BindUtils.postNotifyChange(null, null, this, "postagensTurma");
		BindUtils.postNotifyChange(null, null, this, "linhasAluno");
	}

	public boolean isBuscaGlobal() {
		return buscaGlobal;
	}

	public void setBuscaGlobal(boolean buscaGlobal) {
		this.buscaGlobal = buscaGlobal;
	}

	/**
	 * monta tabela de imagens do album da turma
	 * 
	 * @param evt
	 *            evento disparado pelo ator
	 */

	@Command
	public void montaTabelaImagens() {
		/*
		 * if (evt != null) { largura = evt.getDesktopWidth(); altura =
		 * evt.getDesktopHeight() - 200;
		 * 
		 * 
		 * }
		 */
		largura = 1000;
		altura = 500;
		BindUtils.postNotifyChange(null, null, this, "largura");
		BindUtils.postNotifyChange(null, null, this, "altura");
		int inseridos = 0;
		linhasPostagem = new ArrayList<List<Postagem>>();

		List<Postagem> linhaPostagem = new ArrayList<Postagem>();

		for (Postagem p : postagensTurma) {
			if ((largura) / 200 < (inseridos + 1)) {
				linhasPostagem.add(linhaPostagem);
				inseridos = 0;
				linhaPostagem = new ArrayList<Postagem>();
			}
			linhaPostagem.add(p);
			if (p.getImagem() != null)
				inseridos++;

		}

		if (linhaPostagem.size() > 0)
			linhasPostagem.add(linhaPostagem);

		BindUtils.postNotifyChange(null, null, this, "postagensTurma");
		BindUtils.postNotifyChange(null, null, this, "linhasPostagem");
	}

	public List<List<Aluno>> getLinhasAluno() {
		return linhasAluno;
	}

	public void setLinhasAluno(List<List<Aluno>> linhasAluno) {
		this.linhasAluno = linhasAluno;
	}

	public List<List<Postagem>> getLinhasPostagem() {
		return linhasPostagem;
	}

	public void setLinhasPostagem(List<List<Postagem>> linhasPostagem) {
		this.linhasPostagem = linhasPostagem;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<String> getSemestres() {
		return semestres;
	}

	public List<Postagem> getPostagensTurma() {
		return postagensTurma;
	}

	public Turma getTurma() {
		return turma;
	}

	public int getLargura() {
		return largura;
	}

	/**
	 * Efetua uma pesquisa por nome de aluno na página de turmas depois de
	 * filtrada a pesquisa, monta a tabela novamente com os resultados obtidos.
	 */
	@Command
	@NotifyChange({ "filtraAlunos", "emptyMessage" })
	public void pesquisar() {
		List<Aluno> resultados = new ArrayList<Aluno>();
		if (buscaGlobal) {
			if (pesquisa != null) {
				if (pesquisa.trim().equals("")) {
					resultados = new AlunoBusiness().getAlunos();
				} else {
					for (Aluno aluno : alunos)
						if (aluno.getNome().trim().toLowerCase()
								.contains(pesquisa.trim().toLowerCase()))
							resultados.add(aluno);
				}
			}

		} else {
			if (pesquisa != null) {
				if (pesquisa.trim().equals("")) {
					resultados = new AlunoBusiness().getAlunos(turma);
				} else {
					filtraAlunos = new AlunoBusiness().getAlunos(turma);
					for (Aluno aluno : filtraAlunos)
						if (aluno.getNome().trim().toLowerCase()
								.contains(pesquisa.trim().toLowerCase()))
							resultados.add(aluno);
				}
			} else {
				resultados = new AlunoBusiness().getAlunos(turma);

			}
		}
		filtraAlunos = resultados;
		montaTabela(null);

	}

	/**
	 * Seleciona um curso pelo combobox de cursos da página de turmas
	 * 
	 * @param curso
	 *            Curso selecionado
	 */
	@Command
	public void selecionaCurso(@BindingParam("curso") int curso) {
		cursoSelecionado = cursos.get(curso);
		filtraAlunos = new AlunoBusiness().getTodosCurso(cursoSelecionado);
		montaTabelaTodos(null);

	}

	/**
	 * Faz uma pesquisa por aluno na página de todos alunos, só que agora uma
	 * pesquisa global dentre os alunos de um dado curso
	 */
	@Command
	@NotifyChange({ "filtraAlunos", "emptyMessage" })
	public void pesquisarTodos() {
		List<Aluno> resultados = new ArrayList<Aluno>();
		if (pesquisa != null) {
			if (pesquisa.trim().equals("")) {
				resultados = new AlunoBusiness()
						.getTodosCurso(cursoSelecionado);
			} else {
				for (Aluno aluno : filtraAlunos)
					if (aluno.getNome().trim().toLowerCase()
							.contains(pesquisa.trim().toLowerCase()))
						resultados.add(aluno);
			}
		} else {
			resultados = new AlunoBusiness().getTodosCurso(cursoSelecionado);

		}

		filtraAlunos = resultados;
		montaTabela(null);

	}

	/**
	 * Limpa o campo de pesquisa
	 */
	@Command("limparPesquisa")
	public void limparPesquisa() {
		filtraAlunos = new AlunoBusiness().getAlunos(turma);
		montaTabela(null);
	}

	/**
	 * Redireciona para o perfil de um aluno selecionado
	 * 
	 * @param facebookId
	 *            Facebook id do aluno selecionado
	 */
	@Command
	public void verPerfil(@BindingParam("facebookId") String facebookId) {
		if (facebookId != null)
			Executions.sendRedirect("perfil.zul?id=" + facebookId);
	}

	/**
	 * Chama a página de convite para o aplicativo.
	 */
	@Command
	public void convidar() {
		Executions.sendRedirect("convida.zul");
	}

	@Command
	public void pesquisaGlobal(@BindingParam("global") Checkbox cbx,
			@BindingParam("selectTurma") Combobox comboTurma) {

		comboTurma.setDisabled(cbx.isChecked());
		comboTurma.setSelectedItem(null);
		if (cbx.isChecked()) {
			filtraAlunos = new AlunoBusiness().getAlunos();
			buscaGlobal = true;
		} else {
			buscaGlobal = false;
			filtraAlunos = new AlunoBusiness().getAlunos(turma);
		}
		montaTabela(null);
		BindUtils.postNotifyChange(null, null, this, "buscaGlobal");

	}

	/**
	 * redireciona pro albúm da turma
	 */
	@Command
	public void verAlbum() {

		Clients.evalJavaScript("album()");

	}

	/*
	 * @Command public void verTodosAlunos() {
	 * Clients.evalJavaScript("verTodos()");
	 * 
	 * }
	 */

	/**
	 * Redireciona para o perfil de um aluno
	 */
	@Command
	public void goProfile() {
		Executions.sendRedirect("perfil.zul?id="
				+ ((Aluno) Sessions.getCurrent().getAttribute("aluno"))
						.getFacebookId());

	}

	/**
	 * volta para página de turma
	 */
	@Command
	public void voltaTurma() {
		Clients.evalJavaScript("voltaTurma()");

	}

	/**
	 * redireciona para a página de ver todos alunos
	 */
	@Command
	public void verTodos() {
		Clients.evalJavaScript("verTodos()");

	}

	/**
	 * faz a mudança de {@link Turma} e {@link Curso} quando o {@link Aluno}
	 * seleciona outra {@link Turma} e outro {@link Curso} para ver,
	 * recarregando o mural e a grade de {@link Aluno}s
	 * 
	 */
	@Command
	public void trocaTurma() {
		Clients.evalJavaScript("fadeOut()");
		if (turmaSelecionada != null && cursoSelecionado != null) {

			turma = turmaBusiness.getTurmaCurso(Integer
					.parseInt(turmaSelecionada.substring(0,
							turmaSelecionada.indexOf(" "))), Integer
					.parseInt(turmaSelecionada.substring(
							turmaSelecionada.indexOf("º") - 1,
							turmaSelecionada.indexOf("º"))), cursoSelecionado);
			// monta a string de descrição de turma com base nos dados da turma
			// retornada
			if (turma != null) {
				descricao = "Turma do " + turma.getSemestre()
						+ "º semestre de " + turma.getAno();
				System.out.println(cursoSelecionado.getCurso());
				filtraAlunos = new AlunoBusiness().getAlunosCurso(turma,
						cursoSelecionado);
				postagensTurma = new PostagemBusiness().getPostagens(turma);
				montaTabela(null);
				BindUtils.postNotifyChange(null, null, this, "descricao");
			}
		}
	}

	/**
	 * Retorna para a página zul a descrição da data da {@link Postagem}.
	 * 
	 * @param dataHora
	 *            Data e hora da publicação da {@link Postagem}.
	 * @param label
	 *            {@link Label} que mostrará a data.
	 */
	@Command
	public void descricaoDataPostagem(
			@BindingParam("dataHora") Timestamp dataHora,
			@BindingParam("label") Label label) {
		if (dataHora != null) {
			if (dataHora
					.toString()
					.substring(0, 11)
					.equals(new Timestamp(new Date().getTime()).toString()
							.substring(0, 11)))
				/*
				 * monta a String de descrição de data da postagem, faz uma
				 * conversão de tipo de data.
				 */

				label.setValue("Hoje, "
						+ new SimpleDateFormat("HH:mm").format(dataHora));
			else
				label.setValue(new SimpleDateFormat("MM/dd/yyyy, HH:mm")
						.format(dataHora));
		}
	}

	/**
	 * Salva a {@link Postagem} criada pelo usuário e atualiza a lista.
	 * 
	 * @param texto
	 *            Texto escrito pelo usuário.
	 * @param imagem
	 *            Endereço da imagem.
	 * @param privado
	 *            Se será ou não exibida para todos os ex-alunos.
	 */
	@Command
	public void postar(@BindingParam("txtArea") Textbox txtArea,
			@BindingParam("texto") String texto,
			@BindingParam("privado") boolean privado) {
		String imagem = null;
		if (imgPostagem != null) {
			imagem = FileManager.saveFileInputSream(imgPostagem, imgExtensao);
			String url = ConfHandler.getConf("FILE.PATH");

			File img = new File(url + imagem);
			File img_s = new File(url + "s-" + imagem);
			System.out.println(img.getAbsolutePath());
			System.out.println(img_s.getAbsolutePath());
			try {
				BufferedImage bufimage = ImageIO.read(img);

				BufferedImage bISmallImage = Scalr.resize(bufimage, 141, 110); // after
																				// this
																				// line
																				// my
																				// dimensions
																				// in
																				// bISmallImage
																				// are
																				// correct!
				ImageIO.write(bISmallImage, imgExtensao, img_s); // but my
																	// smallImage
																	// has the
																	// same
																	// dimension
																	// as the
																	// original
																	// foto
			} catch (Exception e) {
				System.out.println(e.getMessage()); // FORNOW: added just to be
													// sure
			}
		}
		if (imgPostagem == null || (imgPostagem != null && imagem != null)) {
			Postagem postagem = new Postagem((Aluno) Sessions.getCurrent()
					.getAttribute("aluno"), turma, privado, texto, imagem,
					new Timestamp(new Date().getTime()), "s-" + imagem);
			imgPostagem = null;
			if (new PostagemBusiness().salvar(postagem)) {
				postagensTurma.add(0, postagem);
				BindUtils.postNotifyChange(null, null, this, "postagensTurma");
				txtArea.setText(null);
				txtArea.setPlaceholder("Escreva algo aqui.");
				imgPostagem = null;
				BindUtils.postNotifyChange(null, null, this, "imgPostagem");

				return;
			}
		}

		Messagebox.show(
				"Não foi possível publicar, tente novamente mais tarde.",
				"Erro", Messagebox.OK, Messagebox.ERROR);
		imgPostagem = null;
		txtArea.setText(null);
		BindUtils.postNotifyChange(null, null, this, "imgPostagem");

	}

	/**
	 * Faz upload de uma imagem jpg, png, ou gif para ser postada no mural da
	 * turma.
	 * 
	 * @param evt
	 *            Evento onUpload
	 */
	@Command
	public void upload(@BindingParam("evt") UploadEvent evt) {
		String[] extensoes = new String[] { "jpg", "png", "gif", "jpeg" };

		for (String s : extensoes)
			if (evt.getMedia().getName().contains(s)) {
				imgPostagem = evt.getMedia().getStreamData();
				imgExtensao = s;
				nomeImg = evt.getMedia().getName();
				BindUtils.postNotifyChange(null, null, this, "imgPostagem");

				BindUtils.postNotifyChange(null, null, this, "nomeImg");
				return;
			}

		Messagebox.show("Este não é um arquivo de imagem.", "Formato inválido",
				Messagebox.OK, Messagebox.INFORMATION);
		imgPostagem = null;
	}

	public String getNomeImg() {
		return nomeImg;
	}

	public void setNomeImg(String nomeImg) {
		this.nomeImg = nomeImg;
	}

	/**
	 * Abre a imagem numa window para o usuário poder ver a imagem em tamanho
	 * maior.s
	 * 
	 * @param window
	 *            Janela onde será exibida a imagem
	 * @param imgSrc
	 *            Endereço da imagem.
	 */
	@Command
	public void verImagem(@BindingParam("window") Window window,
			@BindingParam("imgSrc") String imgSrc) {
		try {

			org.zkoss.image.AImage img = new org.zkoss.image.AImage(
					ConfHandler.getConf("FILE.PATH") + imgSrc);
			((Image) window.getChildren().get(0)).setContent(img);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		window.doPopup();

	}

	@Command
	public void next(@BindingParam("window") Window window) {
		if (indiceImagem == urlPostagens.size() - 1)
			this.indiceImagem = 0;
		else
			this.indiceImagem++;

		try {
			org.zkoss.image.AImage img = new org.zkoss.image.AImage(
					ConfHandler.getConf("FILE.PATH")
							+ urlPostagens.get(indiceImagem));
			((Image) window.getChildren().get(0)).setContent(img);
		} catch (java.io.IOException | java.lang.IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}

	@Command
	public void back(@BindingParam("window") Window window) {
		if (indiceImagem == 0)
			this.indiceImagem = urlPostagens.size() - 1;
		else
			this.indiceImagem--;

		try {
			org.zkoss.image.AImage img = new org.zkoss.image.AImage(
					ConfHandler.getConf("FILE.PATH")
							+ urlPostagens.get(indiceImagem));
			((Image) window.getChildren().get(0)).setContent(img);
		} catch (java.io.IOException | java.lang.IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}

	@Command
	public void inicializa_album(@BindingParam("window") Window window) {
		this.indiceImagem = 0;
		try {
			org.zkoss.image.AImage img = new org.zkoss.image.AImage(
					ConfHandler.getConf("FILE.PATH") + urlPostagens.get(0));
			((Image) window.getChildren().get(0)).setContent(img);
		} catch (java.io.IOException | java.lang.IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra o popup com informações do {@link Aluno}
	 * 
	 * @param popup
	 *            Popup que conterá as informações do {@link Aluno}
	 * @param aluno
	 *            {@link Aluno} selecionado que terá suas informações exibidas
	 */
	@Command
	public void showPopup(@BindingParam("popup") Window popup,
			@BindingParam("alunoSelect") Aluno aluno) {
		alunoSelect = aluno;
		BindUtils.postNotifyChange(null, null, null, "alunoSelect");
		popup.doPopup();

	}

	/**
	 * Esconde o popup de informações de {@link Aluno}
	 * 
	 * @param popup
	 *            Popup a ser escondido.
	 */
	@Command
	public void hidePopup(@BindingParam("popup") Window popup) {

		popup.setVisible(false);
	}

	/**
	 * Carrega uma imagem para ser exibida no elemento img
	 * 
	 * @param img
	 *            imagem a ser carregada
	 * @param imgPath
	 *            caminho da imagem
	 */
	@Command
	public void carregarImagem(@BindingParam("imagem") Image img,
			@BindingParam("imgSrc") String imgPath) {
		if (imgPath != null) {
			try {
				org.zkoss.image.AImage image = new org.zkoss.image.AImage(
						ConfHandler.getConf("FILE.PATH") + imgPath);
				img.setContent(image);
			} catch (java.io.IOException | java.lang.IndexOutOfBoundsException e) {
				System.err.println("Arquivo inexistente!");
			}
			BindUtils.postNotifyChange(null, null, this, "imagem");
		}

	}

}