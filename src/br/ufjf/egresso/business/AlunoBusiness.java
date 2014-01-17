package br.ufjf.egresso.business;

import org.hibernate.HibernateException;

import br.ufjf.egresso.model.Aluno;
import br.ufjf.egresso.persistent.impl.AlunoDAO;

public class AlunoBusiness extends GenericBusiness {
	private AlunoDAO alunoDao;

	public AlunoBusiness() {
		alunoDao = new AlunoDAO();
	}

	public boolean alunoCadastrado(String facebookId)
			throws HibernateException, Exception {
		return alunoDao.getAluno(facebookId) != null;
	}

	public Aluno getAluno(String facebookId) {
		return alunoDao.getAluno(facebookId);
	}
	
	public boolean checaLogin(Aluno aluno) throws HibernateException, Exception {
        if (aluno != null) {
                AlunoDAO alunoDAO = new AlunoDAO();
                aluno = alunoDAO.getAluno(aluno.getFacebookId());
                if (aluno != null) {
                        return true;
                }
        }
        return false;
}

}