package br.com.ciandt.handson.competicao.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ciandt.handson.competicao.dto.CompeticaoDto;
import br.com.ciandt.handson.competicao.model.Competicao;
import br.com.ciandt.handson.competicao.model.Competidor;
import br.com.ciandt.handson.competicao.model.Etapa;
import br.com.ciandt.handson.competicao.model.Local;
import br.com.ciandt.handson.competicao.model.Modalidade;
import br.com.ciandt.handson.competicao.repository.CompeticaoRepository;

@Service
public class CompeticaoService {	
	private CompeticaoRepository competicaoRepository;

	@Autowired
	public CompeticaoService(CompeticaoRepository competicaoRepository) {
		this.competicaoRepository = competicaoRepository;
	}

	public List<Competicao> getByModalidade(String modalidade){
		if(StringUtils.isEmpty(modalidade)){
			List<Competicao> competicaoList = (List<Competicao>) competicaoRepository.findAll();
			competicaoList.sort((o1, o2) -> {
				return o1.getInicio().compareTo(o2.getInicio());
			});
			
			return competicaoList;
		}else{
			try {
				return competicaoRepository.findAllByModalidadeOrderByInicio(validaModalidade(modalidade.toUpperCase()));
			} catch (Exception e) {
				return new ArrayList();
			}
		}
		
	}
	
	@Transactional
	public void delete(Long id){
		competicaoRepository.delete(id);
	}
	
	@Transactional
	public Competicao save(CompeticaoDto competicaoDto) throws Exception {
		Competicao competicao = buildCompeticao(competicaoDto);

		validaCompeticao(competicao);

		return competicaoRepository.save(competicao);

	}

	public void validaCompeticao(Competicao competicao) throws Exception {
		validaTempoMinimo(competicao.getInicio(), competicao.getFim());
		validaCompetidor(competicao.getCompetidorA(), competicao.getCompetidorB(), competicao.getEtapa());
		validaMaximoCompeticoesNoLocal(competicao);
		validaCompeticaoMesmoPeriodo(competicao);
	}

	private void validaMaximoCompeticoesNoLocal(Competicao competicao) throws Exception {
		Calendar begin = Calendar.getInstance();
		begin.setTime(competicao.getInicio().getTime());
		begin.set(Calendar.MILLISECOND, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.HOUR, 0);

		Calendar end = Calendar.getInstance();
		end.setTime(competicao.getInicio().getTime());
		end.set(Calendar.MILLISECOND, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.HOUR,23);

		List<Competicao> competicaoList = competicaoRepository.findAllByLocalAndInicioBetween(competicao.getLocal(), begin, end );
		if (competicaoList.size() > 4){
			if (competicao.getId() == null){
				throw new Exception("O limite de 4 competições para esse local ja foi atingido");
			}
			if (! competicaoList.stream().anyMatch(comp -> comp.equals(competicao.getId()))){
				throw new Exception("O limite de 4 competições para esse local ja foi atingido");
			}			
		}		
	}

	private void validaCompetidor(Competidor competidorA, Competidor competidorB, Etapa etapa) throws Exception {
		if (competidorA.equals(competidorB) && !(etapa.equals(Etapa.SEMI_FINAL)|| etapa.equals(Etapa.FINAL))){
			throw new Exception("Só é permitido o cadastro de comepetidores iguais a partir da semi final");
		}

	}

	private void validaCompeticaoMesmoPeriodo(Competicao competicao) throws Exception {
		Competicao competicaoMesmoPeriodo = 
				competicaoRepository.findFirstByLocalAndModalidadeAndInicioBetweenAndFimBetween(competicao.getLocal(), 
						competicao.getModalidade(),
						competicao.getInicio(), 
						competicao.getFim(), 
						competicao.getInicio(), 
						competicao.getFim());
		if (competicaoMesmoPeriodo != null){
			if (competicao.getId() == null){
				throw new Exception("Já existe uma competição para a modalidade " + competicao.getModalidade() + " nesse período");
			}			
			if(!competicao.getId().equals(competicaoMesmoPeriodo.getId())){
				throw new Exception("Já existe uma competição para a modalidade " + competicao.getModalidade() + " nesse período");

			}
		}
	}

	private void validaTempoMinimo(Calendar dtInicio, Calendar dtFim) throws Exception {
		Instant fim = Instant.ofEpochMilli(dtFim.getTimeInMillis());
		Instant inicio =Instant.ofEpochMilli(dtInicio.getTimeInMillis()).plus(Duration.ofMinutes(30));

		Boolean tempoMinimo = fim.isAfter(inicio);

		if(!tempoMinimo){
			throw new Exception("O período da competição é inferior a 30 minutos");
		}		
	}

	public Competicao buildCompeticao(CompeticaoDto competicaoDto) throws Exception{
		DateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		Competicao competicao = new Competicao(); 
		
		competicao.setCompetidorA(validaCompetidor(competicaoDto.getCompetidorA().toUpperCase()));
		competicao.setCompetidorB(validaCompetidor(competicaoDto.getCompetidorB().toUpperCase()));
		competicao.setEtapa(validaEtapa(competicaoDto.getEtapa().toUpperCase()));		

		try {
			Calendar dataFim = Calendar.getInstance();
			dataFim.setTime(dt.parse(competicaoDto.getFim()));
			competicao.setFim(dataFim);
		} catch (ParseException e) {
			throw new Exception("Data fim inválida");
		}

		try{
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.setTime(dt.parse(competicaoDto.getInicio()));
			competicao.setInicio(dataInicio);
		}catch(ParseException e){
			throw new Exception("Data inicio inválida");
		}

		competicao.setLocal(validaLocal(competicaoDto.getLocal().toUpperCase()));
		competicao.setModalidade(validaModalidade(competicaoDto.getModalidade().toUpperCase()));

		return competicao;

	}

	private Modalidade validaModalidade(String modalidade) throws Exception {
		List<Modalidade> modalidades = Arrays.asList(Modalidade.values());
		List<Modalidade> modalidadeFilteredList = modalidades.stream().filter(mdl -> mdl.name().equals(modalidade)).collect(Collectors.toList());
		
		if(modalidadeFilteredList.size() == 0){
			throw new Exception("Modalidade inválida");
		}
		
		return modalidadeFilteredList.get(0);
	}

	private Local validaLocal(String local) throws Exception {
		List<Local> locais = Arrays.asList(Local.values());
		List<Local> localFilteredList = locais.stream().filter(lcl -> lcl.name().equals(local)).collect(Collectors.toList());
		
		if(localFilteredList.size() == 0){
			throw new Exception("Local inválido");
		}
		
		return localFilteredList.get(0);
	}

	private Etapa validaEtapa(String etapa) throws Exception {
		List<Etapa> etapas = Arrays.asList(Etapa.values());
		List<Etapa> etapaFilteredList = etapas.stream().filter(etp -> etp.name().equals(etapa)).collect(Collectors.toList());
		
		if(etapaFilteredList.size() == 0 ){
			throw new Exception("Etapa inválida");
		}
		
		return etapaFilteredList.get(0);
	}

	private Competidor validaCompetidor(String competidor) throws Exception {
		List<Competidor> competidores = Arrays.asList(Competidor.values());
		List<Competidor> competidorFilteredList = competidores.stream().filter(comp -> comp.name().equals(competidor)).collect(Collectors.toList());
		
		if(competidorFilteredList.size() == 0){
			throw new Exception("Competidor inválido");
		}
		
		return competidorFilteredList.get(0);
		
	}
	
	@Transactional
	public Competicao update(CompeticaoDto competicaoDto) throws Exception {
		if (competicaoDto.getId() == null){
			throw new Exception("id nao preenchido");
		}

		Competicao competicao = competicaoRepository.findOne(competicaoDto.getId());

		if (competicao == null){
			throw new Exception("competicao não encontrada");
		}
		
		Competicao novaCompeticao = buildCompeticao(competicaoDto);
		
		competicao.setCompetidorA(novaCompeticao.getCompetidorA());
		competicao.setCompetidorB(novaCompeticao.getCompetidorB());
		competicao.setEtapa(novaCompeticao.getEtapa());
		competicao.setFim(novaCompeticao.getFim());
		competicao.setInicio(novaCompeticao.getInicio());
		competicao.setLocal(novaCompeticao.getLocal());
		competicao.setModalidade(novaCompeticao.getModalidade());
		
		validaCompeticao(competicao);		
		return competicaoRepository.save(competicao);
	}



}
