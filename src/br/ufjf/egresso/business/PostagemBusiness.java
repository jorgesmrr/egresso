package br.ufjf.egresso.business;

import java.util.List;

import br.ufjf.egresso.model.Curso;
import br.ufjf.egresso.model.Postagem;
import br.ufjf.egresso.model.Turma;
import br.ufjf.egresso.persistent.PostagemDAO;

/**
 * Classe para intermediar o acesso às informações da classe {@link Postagem}.
 * 
 * @author Jorge Augusto da Silva Moreira, Eduardo Rocha Soares
 * 
 */
public class PostagemBusiness {
	private PostagemDAO postagemDao;

	public PostagemBusiness() {
		postagemDao = new PostagemDAO();
	}

	/**
	 * Retorna uma {@link Postagem}.
	 * 
	 * @param id
	 *            O ID do {@link Postagem}.
	 * @return {@link Postagem}.
	 */
	public Postagem getPostagem(int id) {
		return postagemDao.getPostagem(id);
	}

	/**
	 * Edita uma {@link Postagem} e salva no banco.
	 * 
	 * @param postagem
	 *            {@link Postagem} a ser editada.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean editar(Postagem postagem) {
		return postagemDao.editar(postagem);
	}

	/**
	 * Exclui uma {@link Postagem} do banco.
	 * 
	 * @param postagem
	 *            {@link Postagem} a ser excluída.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean exclui(Postagem postagem) {
		return postagemDao.exclui(postagem);
	}

	/**
	 * Salva uma nova {@link Postagem} no banco.
	 * 
	 * @param novaPostagem
	 *            {@link Postagem} a ser salva.
	 * @return {@link true} se houve sucesso; {@link false} se não.
	 */
	public boolean salvar(Postagem novaPostagem) {
		return postagemDao.salvar(novaPostagem);
	}

	/**
	 * Retorna uma {@link List} de {@link Postagem}s de uma turma.
	 * 
	 * @param turma
	 *            Turma das {@link Postagem}s a serem obtidas.
	 * @return Uma {@link List} de {@link Postagem}.
	 */
	public List<Postagem> getPostagens(Turma turma) {
		return postagemDao.getPostagens(turma);
	}

}