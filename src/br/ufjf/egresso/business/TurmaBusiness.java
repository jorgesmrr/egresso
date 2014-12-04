package br.ufjf.egresso.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.egresso.model.Curso;
import br.ufjf.egresso.model.Turma;
import br.ufjf.egresso.persistent.TurmaDAO;

/**
 * Classe para intermediar o acesso às informações da classe {@link Turma}
 * 
 * @author Eduardo Rocha Soares, Jorge Augusto da Silva Moreira, Thiago Goldoni
 * 
 */
public class TurmaBusiness {

	private List<String> errors;
	private TurmaDAO turmaDao;

	public TurmaBusiness() {
		this.errors = new ArrayList<String>();
		turmaDao = new TurmaDAO();
	}

	/**
	 * @return lista de {@link String} descrevendo os erros resultados da
	 *         validação
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**Obtem todos os {@link Turma}s do banco.
	 * 
	 * @return Uuma {@link List} de {@link Turma}s.
	 */
	public List<Turma> getTodas() {
		return new TurmaDAO().getTodas();
	}

	/**
	 * Executa a validação de uma {@link Turma}.
	 * 
	 * @param turma
	 *            {@link Turma} a ser validada.
	 * @return Retorna {@link true} caso erros não sejam encontrados; {@link
	 *         false} caso pelo menos 1 erro seja encontrado. Para obter a lista
	 *         de erros, ver {@link #getErrors()}.
	 */
	public boolean validar(Turma turma) {
		errors.clear();

		validaSemestre(turma.getSemestre());
		return errors.size() == 0;
	}
	
	private void validaSemestre(int semestre) {
		if (!(semestre == 1 || semestre == 2))
			errors.add("Semestre incorreto;\n");
	}

	/**Edita uma {@link Turma} e salva no banco.
	 * 
	 * @param turma {@link Turma} a ser editada.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean editar(Turma turma) {
		return turmaDao.editar(turma);
	}

	/**Exclui uma {@link Turma} do banco.
	 * 
	 * @param turma {@link Turma} a ser excluída.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean exclui(Turma turma) {
		return turmaDao.exclui(turma);
	}

	/**Salva uma nova {@link Turma} no banco.
	 * 
	 * @param turma {@link Turma} a ser salva.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean salvar(Turma novaTurma) {
		return turmaDao.salvar(novaTurma);
	}	

	/**
	 * Retorna uma {@link Turma}.
	 * 
	 * @param id
	 *            O ID da {@link Turma}.
	 * @return {@link Turma}.
	 */
	public Turma getTurma(int id) {
		return turmaDao.getTurma(id);
	}

	/**
	 * Retorna uma {@link Turma} a partir do ano e semestre dados.
	 * 
	 * @param ano
	 * @param semestre
	 * @return A {@link Turma} correspondente.
	 */
	public Turma getTurma(int ano, int semestre) {
		return turmaDao.getTurma(ano, semestre);
	}
	public Turma getTurmaCurso(int ano, int semestre, Curso curso) {
		return turmaDao.getTurmaCurso(ano, semestre, curso);
	}
	

	/**
	 * Retorna todas as turmas de determinado ano.
	 * 
	 * @param ano Ano desejado.
	 * @return lista de turmas do ano.
	 */
	public List<Turma> getTurmas(int ano) {
		return turmaDao.getTurmas(ano);
	}
	public List<Turma> getTodasCurso(Curso curso) {
		return new TurmaDAO().getTodasCurso(curso);
	}

}
