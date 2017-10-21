package br.com.ciandt.handson.competicao.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;

@Entity
public class Competicao extends AbstractPersistable<Long> {
	private Modalidade modalidade;	
	private Local local;	
	private Calendar inicio;
	private Calendar fim;	
	private Competidor competidorA;	
	private Competidor competidorB;	
	private Etapa etapa;
	
	public Competicao() {
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public Calendar getFim() {
		return fim;
	}

	public void setFim(Calendar fim) {
		this.fim = fim;
	}

	public Competidor getCompetidorA() {
		return competidorA;
	}

	public void setCompetidorA(Competidor competidorA) {
		this.competidorA = competidorA;
	}

	public Competidor getCompetidorB() {
		return competidorB;
	}

	public void setCompetidorB(Competidor competidorB) {
		this.competidorB = competidorB;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}
	
	

	
}
