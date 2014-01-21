package br.ufjf.egresso.business;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import br.ufjf.egresso.model.Aluno;
import br.ufjf.egresso.model.Turma;
import br.ufjf.egresso.persistent.impl.AlunoDAO;
import br.ufjf.egresso.persistent.impl.TurmaDAO;



public class TurmaBusiness extends GenericBusiness{
	
	private List<String> errors;
	private TurmaDAO turmaDao;
	
	public TurmaBusiness() {
		this.errors = new ArrayList<String>();
		turmaDao = new TurmaDAO();
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public boolean turmaCadastrada(String turma) throws HibernateException, Exception{
		TurmaDAO turmaDAO = new TurmaDAO();
		if ((turmaDAO.getTurma(turma) != null))
			return true;
		else return false;
	}
	
	public List<Turma> getTodas(){
		return new TurmaDAO().getTodas();
	}
	public boolean validar(Turma turma) {
		errors.clear();

		validaSemestre(turma.getSemestre());
		return errors.size() == 0;
	}
	public boolean editar(Turma turma) {
		return turmaDao.editar(turma);
	}
	public boolean exclui(Turma turma) {
		return turmaDao.exclui(turma);
	}
	public boolean salvar(Turma novaTurma) {
		return turmaDao.salvar(novaTurma);
	}


	private void validaSemestre(String semestre) {
		if (semestre == null || semestre.trim().length() == 0)
			errors.add("É necessário informar o semestre da turma;\n");
	}

}
