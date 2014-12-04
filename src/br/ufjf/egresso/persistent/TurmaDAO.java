package br.ufjf.egresso.persistent;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.egresso.model.Curso;
import br.ufjf.egresso.model.Turma;

/**
 * Classe responsável por recuperar os dados de {@link Turma}
 * 
 * @author Eduardo Rocha Soares
 *
 */
public class TurmaDAO extends GenericoDAO {

	/**
	 * Recupera todas as {@link Turmas}s do banco de dados
	 * 
	 * @return {@link List} de {@link Turma}s ou {@link null} caso não haja
	 *         nenhuma {@link Turma} cadastrada.
	 */
	@SuppressWarnings("unchecked")
	public List<Turma> getTodas() {
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM Turma AS t ORDER BY t.ano, t.semestre ASC");
			List<Turma> turmas = query.list();

			getSession().close();

			return turmas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Turma> getTodasCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM Turma AS t WHERE t.curso = :curso ORDER BY t.ano, t.semestre ASC");
			query.setParameter("curso", curso);

			List<Turma> turmas = query.list();

			getSession().close();

			return turmas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Recupera uma {@link Turma} do banco através de seu ID
	 * 
	 * @param id
	 *            Id da {@link Turma} a ser recuperada
	 * @return {@link Turma} resultante da busca, {@link null} caso contrário.
	 */
	public Turma getTurma(int id) {
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM Turma AS t WHERE t.id = :id");
			query.setParameter("id", id);
			Turma turma = (Turma) query.uniqueResult();

			getSession().close();

			return turma;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Recupera uma {@link Turma} através de seu ano e semestre
	 * 
	 * @param ano
	 *            Ano da {@link Turma}
	 * @param semestre
	 *            Semestre da {@link Turma}s
	 * @return {@link Turma} ou {@link null} caso não retorne nenhum registro
	 */
	public Turma getTurma(int ano, int semestre) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM Turma AS t WHERE t.ano = :ano AND t.semestre = :semestre");
			query.setParameter("ano", ano);
			query.setParameter("semestre", semestre);
			Turma turma = (Turma) query.uniqueResult();

			getSession().close();

			return turma;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Recura uma {@link List} de {@link Turma}s de um determinado ano.
	 * 
	 * @param ano
	 *            Ano das {@link Turma}s buscadas.
	 * @return {@link List} de {@link Turma} ou {@link null} caso a busca
	 *         retorne vazio.
	 */
	public List<Turma> getTurmas(int ano) {
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM Turma AS t WHERE t.ano = :ano");
			query.setParameter("ano", ano);
			@SuppressWarnings("unchecked")
			List<Turma> turmas = query.list();

			getSession().close();

			return turmas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Turma getTurmaCurso(int ano, int semestre, Curso curso) {

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM Turma AS t WHERE t.ano = :ano AND t.semestre = :semestre AND t.curso = :curso");
			query.setParameter("ano", ano);
			query.setParameter("semestre", semestre);
			query.setParameter("curso", curso);
			Turma turma = (Turma) query.uniqueResult();

			getSession().close();

			return turma;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
