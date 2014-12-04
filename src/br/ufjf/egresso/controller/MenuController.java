package br.ufjf.egresso.controller;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import br.ufjf.egresso.model.Administrador;
import br.ufjf.egresso.model.Aluno;

public class MenuController {
	private Aluno aluno;
	private Administrador admin;
	@Init
	public void init() {
		aluno = (Aluno) Sessions.getCurrent().getAttribute("aluno");
		admin =(Administrador) Sessions.getCurrent().getAttribute("admin");
		System.out.println(admin.getNome());
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Command
	public void sair() {
		Sessions.getCurrent().invalidate();
		Executions.sendRedirect("/index.zul");
	}

	@Command
	public void api() {
		Window window = (Window) Executions.createComponents("/fb/api.zul",
				null, null);
		window.doModal();
	}

}
