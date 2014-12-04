package br.ufjf.egresso.persistent;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.egresso.model.Curso;
import br.ufjf.egresso.model.Postagem;
import br.ufjf.egresso.model.Turma;
/**
 * Classe responsável por manipular os dados de {@link Postagem}
 * @author esoares
 *
 */
public class PostagemDAO extends GenericoDAO {
	/**
	 * Recupera uma {@link Postagem} pelo seu id
	 * @param id
	 * id da {@link Postagem} que será recuperada do banco
	 * @return
	 * {@link Postagem} ou {@link null} caso não seja encontrada
	 * nenhuma {@link Postagem} com aquele id.
	 */
	public Postagem getPostagem(int id) {
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Postagem AS p WHERE p.id = :id");
			query.setParameter("id", id);

			Postagem postagem = (Postagem) query.uniqueResult();

			getSession().close();

			return postagem;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Recupera as {@link Postagem}s de uma {@link Turma} e {@link Curso}
	 * @param turma
	 * {@link Turma} da qual serão recuperadas as {@link Postagem}s 
	 * @param curso
	 * {@link Curso} do qual serão recuperadas as {@link Postagem}s 

	 * @return
	 * {@link List} de {@link Postagem} retornadas da busca ou {@link null} 
	 * caso não exista nenhuma {@link Postagem}
	 */
	@SuppressWarnings("unchecked")
	public List<Postagem> getPostagens(Turma turma) {
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Postagem AS p WHERE p.turma = :turma  ORDER BY data_hora DESC ");
			query.setParameter("turma", turma);
			
			List<Postagem> postagens = query.list();
			getSession().close();

			return postagens;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
