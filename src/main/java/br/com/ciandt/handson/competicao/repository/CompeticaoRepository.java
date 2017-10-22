package br.com.ciandt.handson.competicao.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import br.com.ciandt.handson.competicao.model.Competicao;
import br.com.ciandt.handson.competicao.model.Local;
import br.com.ciandt.handson.competicao.model.Modalidade;

public interface CompeticaoRepository extends CrudRepository<Competicao, Long> {
	List<Competicao> findAllByModalidadeOrderByInicio(Modalidade modalidade);
	
	Competicao findFirstByLocalAndModalidadeAndInicioBetweenAndFimBetween(Local local, Modalidade modalidade,
			Calendar inicioDtInicio, Calendar fimDtInicio, Calendar inicioDtFim, Calendar fimDtFim);

	List<Competicao> findAllByLocalAndInicioBetween(Local local, Calendar inicio, Calendar fim);

}
