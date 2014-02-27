package br.ufjf.egresso.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.egresso.model.Turma;
import br.ufjf.egresso.persistent.GenericoDAO;
import br.ufjf.egresso.persistent.ITurmaDAO;

public class TurmaDAO extends GenericoDAO implements ITurmaDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<Turma> getTodas() {
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM Turma AS t ORDER BY t.semestre");
			List<Turma> turmas = query.list();

			getSession().close();

			return turmas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Turma getTurma(int id) {
		try {
			Query query = getSession()
					.createQuery(
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

}
