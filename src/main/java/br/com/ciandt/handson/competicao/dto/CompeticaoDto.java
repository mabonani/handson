package br.com.ciandt.handson.competicao.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.com.ciandt.handson.competicao.model.Competicao;
import br.com.ciandt.handson.competicao.model.Competidor;
import br.com.ciandt.handson.competicao.model.Etapa;
import br.com.ciandt.handson.competicao.model.Local;
import br.com.ciandt.handson.competicao.model.Modalidade;
import br.com.ciandt.handson.dto.BaseDto;

public class CompeticaoDto implements BaseDto{
	private Long id;
	private String modalidade;	
	private String local;	
	private String inicio;
	private String fim;	
	private String competidorA;	
	private String competidorB;	
	private String etapa;
	
	public CompeticaoDto() {
	}
	
	public CompeticaoDto(Competicao competicao){
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		id = competicao.getId();
		modalidade = competicao.getModalidade().name();
		local = competicao.getLocal().name();
		df.setTimeZone(competicao.getInicio().getTimeZone());
		inicio =  df.format(competicao.getInicio().getTime());
		df.setTimeZone(competicao.getFim().getTimeZone());
		fim = df.format(competicao.getFim().getTime());
		competidorA = competicao.getCompetidorA().name();
		competidorB = competicao.getCompetidorB().name();
		etapa = competicao.getEtapa().name();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getCompetidorA() {
		return competidorA;
	}

	public void setCompetidorA(String competidorA) {
		this.competidorA = competidorA;
	}

	public String getCompetidorB() {
		return competidorB;
	}

	public void setCompetidorB(String competidorB) {
		this.competidorB = competidorB;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	
	
}