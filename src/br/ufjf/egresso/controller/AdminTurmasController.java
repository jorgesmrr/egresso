package br.ufjf.egresso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.egresso.business.TurmaBusiness;
import br.ufjf.egresso.model.Administrador;
import br.ufjf.egresso.model.Turma;
/**
 * Classe que controla a página gerencia_turmas.zul
 * @author Eduardo Rocha Soares, Jorge Moreira da Silva
 *
 */
public class AdminTurmasController {
	private TurmaBusiness turmaBusiness = new TurmaBusiness();
	private Turma novaTurma = new Turma();
	private Map<Integer, Turma> editTemp = new HashMap<Integer, Turma>();
	private List<Turma> todasTurmas;
	private List<Turma> filterTurmas;
	private String filterInt;
	private Administrador admin;
	@Init
	public void init() {

		admin = ((Administrador) Sessions.getCurrent().getAttribute("admin"));
		todasTurmas =  turmaBusiness.getTodasCurso(admin.getCurso());
		filterTurmas = todasTurmas;
		

	}
	public List<Turma> getFilterTurmas() {
		return filterTurmas;
	}

	public Turma getNovaTurma() {
		return this.novaTurma;
	}

	public void setNovaTurma(Turma novaTurma) {
		this.novaTurma = novaTurma;
	}

	public String getFilterInt() {
		return filterInt;
	}

	public void setFilterInt(String filterInt) {
		this.filterInt = filterInt;
	}
	/**
	 * Habilita uma determinada {@link Turma} 
	 * no template para edição.
	 * @param turma
	 * {@link Turma}  que será habilitada para edição
	 */
	@Command
	public void changeEditableStatus(@BindingParam("turma") Turma turma) {
		if (!turma.getEditingStatus()) {
			Turma temp = new Turma();
			temp.copy(turma);
			editTemp.put(turma.getId(), temp);
			turma.setEditingStatus(true);
		} else {
			turma.copy(editTemp.get(turma.getId()));
			editTemp.remove(turma.getId());
			turma.setEditingStatus(false);
		}
		refreshRowTemplate(turma);
	}


	/**
	 * Valida a edição da {@link Turma} e realiza a edição chamando
	 * as funções que lidam com o banco de dados
	 * @param turma
	 * {@link Turma} que está sendo validada
	 */
	@Command
	public void confirm(@BindingParam("turma") Turma turma) {
		if (turmaBusiness.validar(turma)) {
			if (!turmaBusiness.editar(turma))
				Messagebox.show("Não foi possível editar o departamento.",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(turma.getId());
			turma.setEditingStatus(false);
			refreshRowTemplate(turma);
		} else {
			String errorMessage = "";
			for (String error : turmaBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * Deleta uma {@link Turma} do banco de dados
	 * @param turma
	 * {@link Turma} a ser deletada
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(@BindingParam("turma") final Turma turma) {
		Messagebox.show(
				"Você tem certeza que deseja excluir a turma do semestre "
						+ turma.getSemestre() + "?", "Confirmação", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (turmaBusiness.exclui(turma)) {
								removeFromList(turma);
								Messagebox
										.show("A turma foi excluida com sucesso.",
												"Sucesso", Messagebox.OK,
												Messagebox.INFORMATION);
							} else {
								String errorMessage = "A turma não pode ser excluída.\n";
								for (String error : turmaBusiness.getErrors())
									errorMessage += error;
								Messagebox.show(errorMessage, "Erro",
										Messagebox.OK, Messagebox.ERROR);
							}

						}
					}
				});
	}
	/**
	 * Remove uma {@link Turma} da {@link List} de turmas 
	 * e notifica o zk da mudança
	 * @param turma
	 * 	{@link Turma} a ser removida da {@link List}
	 */
	public void removeFromList(Turma turma) {
		filterTurmas.remove(turma);
		todasTurmas.remove(turma);
		BindUtils.postNotifyChange(null, null, this, "filterTurmas");
	}
	/**
	 * Notifica o ZK que uma {@link Turma} foi editada
	 * @param turma
	 * 	{@link Turma} que teve os dados atualizados
	 */
	public void refreshRowTemplate(Turma turma) {
		BindUtils.postNotifyChange(null, null, turma, "editingStatus");
	}
	/**
	 * Filtra as {@link Turma}s da {@link List} de acordo 
	 * com uma string de busca e notifica o zk para
	 * mostrar apenas as {@link Turma}s que atendem aquela busca
	 */
	@Command
	public void filtra() {
		filterTurmas = new ArrayList<Turma>();
		for (Turma c : todasTurmas) {
			if (String.valueOf(c.getAno()).contains(filterInt)) {
				filterTurmas.add(c);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterTurmas");
	}
	
	/**
	 * Invoca a janela que conterá os dados para adicionar 
	 * nova turma e limpa esses campos.
	 * @param window
	 * 	Janela que contem os campos de dados para adicionar
	 * nova {@link Turma}
	 */
	@Command
	public void addTurma(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}
	/**
	 * /**
	 * Valida os dados da nova {@link Turma} e 
	 * envia para ser salva no banco , 
	 * se falhar mostra uma mensagem de erro.
	 * @param window 
	 *  Janela que contém os campos de
	 *   informação da {@link Turma}
	 */

	@Command
	public void submitTurma(@BindingParam("window") final Window window) {
		novaTurma.setCurso(admin.getCurso());
		if (turmaBusiness.validar(novaTurma)) {
			if (turmaBusiness.salvar(novaTurma)) {
				todasTurmas.add(novaTurma);
				filterTurmas = todasTurmas;
				notifyTurmas();
				Clients.clearBusy(window);
				Messagebox.show("Turma Adicionada!", "Sucesso",
						Messagebox.OK, Messagebox.INFORMATION);
				limpa();
			} else {
				Clients.clearBusy(window);
				Messagebox.show("A turma não foi adicionada!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			}
		} else {
			String errorMessage = "";
			for (String error : turmaBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * Notifica o zk que houve uma mudança na {@link List}
	 * de {@link Turma} 
	 */
	public void notifyTurmas() {
		BindUtils.postNotifyChange(null, null, this, "filterTurmas");
	}
	/**
	 *Limpa as informações dos campos da janela de adicionar {@link Turma} 
	 */
	public void limpa() {
		novaTurma = new Turma();
		BindUtils.postNotifyChange(null, null, this, "novaTurma");
	}

}
